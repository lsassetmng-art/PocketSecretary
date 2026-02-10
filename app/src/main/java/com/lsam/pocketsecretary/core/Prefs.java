package com.lsam.pocketsecretary.core;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {

    private static final String NAME = "pocket_secretary_prefs";

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    // ---- public API ----

    public static SharedPreferences sp(Context c) {
        return prefs(c);
    }

    public static boolean isOnboarded(Context c) {
        return prefs(c).getBoolean("onboarded", false);
    }

    public static void setOnboarded(Context c, boolean v) {
        prefs(c).edit().putBoolean("onboarded", v).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return prefs(c).getString("default_secretary", "");
    }

    public static void setDefaultSecretary(Context c, String id) {
        prefs(c).edit().putString("default_secretary", id).apply();
    }

    private Prefs() {}
}