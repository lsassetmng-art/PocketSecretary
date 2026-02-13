package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class BackgroundStore {

    private static final String PREF_NAME = "ps_settings";
    private static final String KEY_BACKGROUND_ID = "current_background_id";

    private BackgroundStore() {}

    // ==========================
    // 保存
    // ==========================
    public static void set(Context context, String backgroundId) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        pref.edit()
                .putString(KEY_BACKGROUND_ID, backgroundId)
                .apply();
    }

    // ==========================
    // 取得
    // ==========================
    public static String get(Context context) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(KEY_BACKGROUND_ID, "default");
    }
}
