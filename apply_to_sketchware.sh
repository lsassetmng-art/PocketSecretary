#!/data/data/com.termux/files/usr/bin/bash
set -e

# ===== 設定ここだけ =====
PROJECT_ID="MoneySelfManager"                 # ← SketchwareのプロジェクトID
BASE_PKG="com.lsam.BussinessApp"       # ← Javaパッケージ
# =======================

SKETCH_DIR="/storage/emulated/0/Sketchware/projects/${PROJECT_ID}/app/src/main/java"
TARGET_DIR="${SKETCH_DIR}/$(echo $BASE_PKG | tr '.' '/')"

echo "=== APPLY COMMON JAVA TO SKETCHWARE ==="
echo "Target: $TARGET_DIR"

if [ ! -d "$SKETCH_DIR" ]; then
  echo "❌ Sketchware project not found"
  exit 1
fi

mkdir -p "$TARGET_DIR"

# -------------------------
# SessionManager
# -------------------------
SM_FILE="$TARGET_DIR/SessionManager.java"
if [ -f "$SM_FILE" ]; then
  echo "ℹ SessionManager already exists (skip)"
else
  cat << EOS > "$SM_FILE"
package $BASE_PKG;

public final class SessionManager {

    private static String companyId;

    private SessionManager() {}

    /** call once after login */
    public static void initCompanyId(String cid) {
        if (cid == null || cid.isEmpty()) {
            throw new IllegalArgumentException("company_id is empty");
        }
        companyId = cid;
    }

    /** the only allowed access point */
    public static String requireCompanyId() {
        if (companyId == null) {
            throw new IllegalStateException("company_id not initialized");
        }
        return companyId;
    }

    public static void clear() {
        companyId = null;
    }
}
EOS
  echo "✅ SessionManager.java created"
fi

# -------------------------
# SupabaseClient (thin)
# -------------------------
SB_FILE="$TARGET_DIR/SupabaseClient.java"
if [ -f "$SB_FILE" ]; then
  echo "ℹ SupabaseClient already exists (skip)"
else
  cat << EOS > "$SB_FILE"
package $BASE_PKG;

public final class SupabaseClient {

    private static final String BASE_URL = "https://YOUR_PROJECT.supabase.co/rest/v1/";
    private static final String API_KEY  = "YOUR_ANON_PUBLIC_KEY";

    private SupabaseClient() {}

    // NOTE:
    // - company_id is NOT sent
    // - RLS is trusted
    // - HTTP implementation is done via Sketchware blocks or custom code
}
EOS
  echo "✅ SupabaseClient.java created"
fi

echo "=== COMPLETED ==="
