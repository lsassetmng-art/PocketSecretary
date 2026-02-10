package com.lsam.pocketsecretary.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static boolean isNotifyEnabled(Context c) {
        return sp(c).getBoolean("notify", true);
    }

    public static boolean getBool(Context c, String k, boolean d) {
        return sp(c).getBoolean(k, d);
    }

    public static void putBool(Context c, String k, boolean v) {
        sp(c).edit().putBoolean(k, v).apply();
    }
}
