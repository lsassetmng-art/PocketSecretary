#!/data/data/com.termux/files/usr/bin/bash
set -e

# =========================
# Config
# =========================
ROOT_DIR="$HOME/erp-foundation"
APP_MAIN="$ROOT_DIR/app/src/main"
JAVA_DIR="$APP_MAIN/java"
RES_DIR="$APP_MAIN/res"

# 生成対象パッケージ（固定：共通基盤）
BASE_PKG="foundation"

# =========================
# Helpers
# =========================
mkpkg() {
  local pkg="$1"
  mkdir -p "$JAVA_DIR/$(echo "$pkg" | tr '.' '/')"
}

write_file() {
  local path="$1"
  shift
  mkdir -p "$(dirname "$path")"
  cat > "$path" <<'__CONTENT__'
'"$@"'
__CONTENT__
}

echo "== Create repo skeleton: $ROOT_DIR =="
mkdir -p "$ROOT_DIR/scripts" "$ROOT_DIR/docs"
mkdir -p "$JAVA_DIR" "$RES_DIR/layout" "$RES_DIR/values" "$RES_DIR/xml"

# =========================
# docs
# =========================
cat > "$ROOT_DIR/docs/architecture.md" <<'MD'
# ERP Foundation (Coding-Only)
- This repo is the **source of truth** for coding.
- Build/test are handled elsewhere (PC / CI).
- **Hard rules**
  - company_id is never accessed outside SessionManager (and never exposed publicly).
  - Supabase RLS is assumed; client must not send company_id.
  - SQLite is cache only.
MD

cat > "$ROOT_DIR/docs/coding-rules.md" <<'MD'
# Coding Rules
- No company_id in Repository/Activity/UI.
- No "company_id" in request query/body.
- Fail-fast if session not initialized.
- Keep each class single-responsibility.
MD

# =========================
# Foundation packages
# =========================
mkpkg "foundation.guard"
mkpkg "foundation.session"
mkpkg "foundation.supabase"
mkpkg "foundation.repo"
mkpkg "foundation.cache"
mkpkg "foundation.util"
mkpkg "foundation.model"

# =========================
# foundation/guard/Preconditions.java
# =========================
cat > "$JAVA_DIR/foundation/guard/Preconditions.java" <<'JAVA'
package foundation.guard;

public final class Preconditions {

    private Preconditions() {}

    public static void notEmpty(String v, String name) {
        if (v == null || v.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
    }

    public static void state(boolean ok, String message) {
        if (!ok) throw new IllegalStateException(message);
    }
}
JAVA

# =========================
# foundation/session/SessionManager.java
# - company_id is NOT publicly exposed
# =========================
cat > "$JAVA_DIR/foundation/session/SessionManager.java" <<'JAVA'
package foundation.session;

import foundation.guard.Preconditions;

/**
 * SessionManager (Source of truth for session state)
 *
 * Hard rules:
 * - company_id is managed ONLY here
 * - company_id must NOT be exposed publicly (RLS assumed)
 * - Fail-fast when uninitialized
 */
public final class SessionManager {

    private static volatile SessionManager INSTANCE;

    private final String companyId; // hidden (package-private accessor only)
    private final String userId;

    private SessionManager(String companyId, String userId) {
        Preconditions.notEmpty(companyId, "companyId");
        // userId may be null depending on auth strategy
        this.companyId = companyId;
        this.userId = userId;
    }

    /** Initialize once after login/auth success. Idempotent. */
    public static synchronized void init(String companyId, String userId) {
        if (INSTANCE != null) return;
        INSTANCE = new SessionManager(companyId, userId);
    }

    /** Require initialized session. */
    public static SessionManager get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("SessionManager not initialized");
        }
        return INSTANCE;
    }

    /** Clear session (logout). */
    public static synchronized void clear() {
        INSTANCE = null;
    }

    /** Visible for session-related internal components ONLY (package-private). */
    String internalCompanyId() {
        return companyId;
    }

    /** User id is allowed to be public if needed. */
    public String userId() {
        return userId;
    }
}
JAVA

# =========================
# foundation/session/SessionGuard.java
# =========================
cat > "$JAVA_DIR/foundation/session/SessionGuard.java" <<'JAVA'
package foundation.session;

/**
 * SessionGuard
 * - A tiny helper to enforce init() has happened.
 */
public final class SessionGuard {

    private SessionGuard() {}

    public static void requireInitialized() {
        SessionManager.get(); // fail-fast
    }
}
JAVA

# =========================
# foundation/supabase/SupabaseConfig.java
# =========================
cat > "$JAVA_DIR/foundation/supabase/SupabaseConfig.java" <<'JAVA'
package foundation.supabase;

import foundation.guard.Preconditions;

/**
 * SupabaseConfig
 * - RLS assumed
 * - Use anon key + user JWT (recommended) at runtime
 * - This repo focuses on coding only, so values are injected by app layer.
 */
public final class SupabaseConfig {

    private final String baseUrl;   // e.g. https://xxxx.supabase.co
    private final String anonKey;   // public anon key

    public SupabaseConfig(String baseUrl, String anonKey) {
        Preconditions.notEmpty(baseUrl, "baseUrl");
        Preconditions.notEmpty(anonKey, "anonKey");
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.anonKey = anonKey;
    }

    public String baseUrl() { return baseUrl; }
    public String anonKey() { return anonKey; }
}
JAVA

# =========================
# foundation/supabase/SupabaseClient.java
# - Never accepts company_id
# =========================
cat > "$JAVA_DIR/foundation/supabase/SupabaseClient.java" <<'JAVA'
package foundation.supabase;

import foundation.guard.Preconditions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * SupabaseClient (thin HTTP layer)
 * - company_id must NEVER be sent
 * - RLS is trusted
 * - Auth token (JWT) can be optionally passed from app layer
 */
public final class SupabaseClient {

    private final SupabaseConfig config;
    private volatile String bearerToken; // optional JWT (user session)

    public SupabaseClient(SupabaseConfig config) {
        this.config = config;
    }

    /** Optional: set user JWT for RLS policies that depend on auth.uid() */
    public void setBearerToken(String jwt) {
        this.bearerToken = jwt;
    }

    public String get(String pathAndQuery) throws Exception {
        return request("GET", pathAndQuery, null);
    }

    public String post(String pathAndQuery, String jsonBody) throws Exception {
        Preconditions.notEmpty(jsonBody, "jsonBody");
        return request("POST", pathAndQuery, jsonBody);
    }

    public String patch(String pathAndQuery, String jsonBody) throws Exception {
        Preconditions.notEmpty(jsonBody, "jsonBody");
        return request("PATCH", pathAndQuery, jsonBody);
    }

    private String request(String method, String pathAndQuery, String jsonBody) throws Exception {
        if (pathAndQuery == null || !pathAndQuery.startsWith("/")) {
            throw new IllegalArgumentException("pathAndQuery must start with '/'");
        }

        // Safety: block company_id being sent by mistake
        if (pathAndQuery.contains("company_id")) {
            throw new IllegalStateException("Forbidden: company_id in request");
        }
        if (jsonBody != null && jsonBody.contains("\"company_id\"")) {
            throw new IllegalStateException("Forbidden: company_id in request body");
        }

        String urlStr = config.baseUrl() + pathAndQuery;
        URL url = new URL(urlStr);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod(method);
        c.setRequestProperty("apikey", config.anonKey());
        c.setRequestProperty("Content-Type", "application/json");
        // If you use Supabase REST with JWT:
        // Authorization: Bearer <jwt>
        String jwt = bearerToken;
        if (jwt != null && !jwt.isEmpty()) {
            c.setRequestProperty("Authorization", "Bearer " + jwt);
        } else {
            // fallback (not recommended for authenticated endpoints)
            c.setRequestProperty("Authorization", "Bearer " + config.anonKey());
        }

        if (jsonBody != null) {
            c.setDoOutput(true);
            try (OutputStream os = c.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
            }
        }

        int code = c.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 400 ? c.getErrorStream() : c.getInputStream()
        ));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append('\n');

        if (code >= 400) {
            throw new IllegalStateException("Supabase HTTP " + code + ": " + sb.toString());
        }
        return sb.toString();
    }
}
JAVA

# =========================
# foundation/repo/BaseRepository.java
# =========================
cat > "$JAVA_DIR/foundation/repo/BaseRepository.java" <<'JAVA'
package foundation.repo;

import foundation.session.SessionGuard;
import foundation.supabase.SupabaseClient;

/**
 * BaseRepository
 * - Enforces session initialized
 * - Does NOT expose company_id (RLS assumed)
 */
public abstract class BaseRepository {

    protected final SupabaseClient client;

    protected BaseRepository(SupabaseClient client) {
        this.client = client;
        SessionGuard.requireInitialized();
    }
}
JAVA

# =========================
# foundation/cache/CacheStore.java
# =========================
cat > "$JAVA_DIR/foundation/cache/CacheStore.java" <<'JAVA'
package foundation.cache;

/**
 * CacheStore
 * - Cache ONLY (never source of truth)
 */
public interface CacheStore<T> {

    void put(String key, T value);

    T get(String key);

    void invalidate(String key);

    void clear();
}
JAVA

# =========================
# foundation/util/Clock.java
# =========================
cat > "$JAVA_DIR/foundation/util/Clock.java" <<'JAVA'
package foundation.util;

public final class Clock {
    private Clock() {}
    public static long nowMillis() {
        return System.currentTimeMillis();
    }
}
JAVA

# =========================
# foundation/model/ApiError.java
# =========================
cat > "$JAVA_DIR/foundation/model/ApiError.java" <<'JAVA'
package foundation.model;

/** Minimal error model for logging/diagnostics */
public final class ApiError {
    public final String message;
    public final String detail;

    public ApiError(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }
}
JAVA

# =========================
# Reference XML (design-source)
# =========================
cat > "$RES_DIR/xml/network_security_config.xml" <<'XML'
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
</network-security-config>
XML

cat > "$RES_DIR/values/strings.xml" <<'XML'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- foundation: keep constants here (design-source) -->
    <string name="supabase_base_url">https://YOUR_PROJECT.supabase.co</string>
</resources>
XML

# =========================
# scripts helper
# =========================
cat > "$ROOT_DIR/scripts/tree.txt" <<'TXT'
This repo is coding-only. Build/test later on PC/CI.
TXT

echo "== DONE =="
echo "Created: $ROOT_DIR"
echo ""
echo "Next:"
echo "  cd $ROOT_DIR"
echo "  git init && git add . && git commit -m 'init foundation (coding-only)'"
