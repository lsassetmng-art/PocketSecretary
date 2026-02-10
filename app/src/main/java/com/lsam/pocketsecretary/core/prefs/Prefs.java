package com.lsam.pocketsecretary.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
    private static final String SP = "ps_prefs";

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(SP, Context.MODE_PRIVATE);
    }

    public static boolean getBool(Context c, String key, boolean def) {
        return sp(c).getBoolean(key, def);
    }

    public static void putBool(Context c, String key, boolean v) {
        sp(c).edit().putBoolean(key, v).apply();
    }

    // 通知の明示ON（Play安全：ユーザー操作でONにできる）
    public static boolean isNotifyEnabled(Context c) {
        return getBool(c, "notify_enabled", true);
    }

    public static void setNotifyEnabled(Context c, boolean v) {
        putBool(c, "notify_enabled", v);
    }
}
