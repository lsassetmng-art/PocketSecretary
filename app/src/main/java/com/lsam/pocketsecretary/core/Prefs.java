package com.lsam.pocketsecretary.core;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {

    private static final String NAME = "prefs";

    public static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static final String KEY_ONBOARDED = "onboarded";
    private static final String KEY_DEFAULT_SECRETARY = "default_secretary";

    public static void setOnboarded(Context c, boolean v) {
        sp(c).edit().putBoolean(KEY_ONBOARDED, v).apply();
    }

    public static boolean isOnboarded(Context c) {
        return sp(c).getBoolean(KEY_ONBOARDED, false);
    }

    public static void setDefaultSecretary(Context c, String id) {
        sp(c).edit().putString(KEY_DEFAULT_SECRETARY, id).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return sp(c).getString(KEY_DEFAULT_SECRETARY, "");
    }

    public static boolean isVoiceOn(Context c) {
        return sp(c).getBoolean("voice_on", true);
    }
}