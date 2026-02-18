package com.lsam.pocketsecretary.core.theme;

import android.content.Context;
import android.content.SharedPreferences;

public final class BusinessModeManager {

    private static final String PREFS = "ps_prefs";
    private static final String KEY = "business_mode_enabled";

    private BusinessModeManager() {}

    public static boolean isEnabled(Context context) {
        return prefs(context).getBoolean(KEY, false);
    }

    public static void setEnabled(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY, enabled).apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
