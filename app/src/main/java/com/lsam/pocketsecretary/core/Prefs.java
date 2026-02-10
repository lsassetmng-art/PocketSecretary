package com.lsam.pocketsecretary.core;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String NAME = "pocket_secretary";

    public static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void setOnboarded(Context c, boolean v) {
        sp(c).edit().putBoolean("onboarded", v).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return sp(c).getString("default_secretary", "hiyori");
    }
}
