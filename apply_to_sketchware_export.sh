#!/data/data/com.termux/files/usr/bin/bash
set -e

echo "=== APPLY SessionManager to Sketchware EXPORT ==="

# ===== 設定 =====
APP_NAME="MoneySelfManager"
BASE="/storage/emulated/0/sketchware/export_src"
TARGET="$BASE/$APP_NAME/app/src/main/java"

# Sketchwareのパッケージに合わせて変更可
PKG="com.example.moneyselfmanager.common"
DEST="$TARGET/$(echo $PKG | tr '.' '/')"

# ===== チェック =====
if [ ! -d "$TARGET" ]; then
  echo "❌ Sketchware export_src が見つかりません"
  echo "   $TARGET"
  exit 1
fi

mkdir -p "$DEST"

# ===== SessionManager.java 生成 =====
cat <<'JAVA' > "$DEST/SessionManager.java"
package com.example.moneyselfmanager.common;

/**
 * SessionManager
 *
 * 【絶対ルール】
 * - company_id はここでのみ保持
 * - Supabase RLS 前提
 * - SQLite はキャッシュ用途のみ
 * - 未初期化時は Fail Fast
 */
public final class SessionManager {

    private static SessionManager instance;

    private String companyId;
    private boolean initialized = false;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /** ログイン成功後に1回だけ呼ぶ */
    public synchronized void init(String companyIdFromAuth) {
        if (initialized) return;

        if (companyIdFromAuth == null || companyIdFromAuth.isEmpty()) {
            throw new IllegalStateException("company_id is required");
        }

        this.companyId = companyIdFromAuth;
        this.initialized = true;
    }

    /** company_id 取得（未初期化なら即例外） */
    public String requireCompanyId() {
        if (!initialized) {
            throw new IllegalStateException("SessionManager not initialized");
        }
        return companyId;
    }

    public boolean isInitialized() {
        return initialized;
    }

    /** ログアウト時 */
    public synchronized void clear() {
        companyId = null;
        initialized = false;
    }
}
JAVA

echo "✅ SessionManager.java created"
echo "   $DEST/SessionManager.java"
echo "=== COMPLETED ==="
