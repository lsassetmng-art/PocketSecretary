package com.lsam.pocketsecretary.core;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String NAME = "pocket_secretary";

    // ---- shared prefs accessor ----
    public static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    // ---- onboarding ----
    public static void setOnboarded(Context c, boolean v) {
        sp(c).edit().putBoolean("onboarded", v).apply();
    }

    public static boolean isOnboarded(Context c) {
        return sp(c).getBoolean("onboarded", false);
    }

    // ---- default secretary ----
    public static void setDefaultSecretary(Context c, String id) {
        sp(c).edit().putString("default_secretary", id).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return sp(c).getString("default_secretary", "hiyori");
    }
}
