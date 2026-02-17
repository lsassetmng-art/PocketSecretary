package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class BackgroundStore {

    private static final String PREF_NAME = "ps_settings";
    private static final String KEY_BACKGROUND_ID = "current_background_id";

    // Canonical default pack
    private static final String DEFAULT_PACK_ID = "desk_set_001";

    private BackgroundStore() {}

    // ==========================
    // 保存
    // ==========================
    public static void set(Context context, String backgroundId) {

        if (backgroundId == null || backgroundId.trim().isEmpty()) {
            backgroundId = DEFAULT_PACK_ID;
        }

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        pref.edit()
                .putString(KEY_BACKGROUND_ID, backgroundId)
                .apply();
    }

    // ==========================
    // 取得（旧値defaultを無効化）
    // ==========================
    public static String get(Context context) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String id = pref.getString(KEY_BACKGROUND_ID, null);

        // 🔥 旧バージョン互換処理
        if (id == null || id.isEmpty() || "default".equals(id)) {
            return DEFAULT_PACK_ID;
        }

        return id;
    }
}
