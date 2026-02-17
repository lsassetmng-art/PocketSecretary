package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class MorningSettingsStore {

    private static final String PREF = "ps_settings";

    private static final String KEY_ENABLED = "morning_enabled";
    private static final String KEY_HOUR = "morning_hour";
    private static final String KEY_MINUTE = "morning_minute";
    private static final String KEY_BEFORE = "before_minutes";

    public static final boolean DEFAULT_ENABLED = true;
    public static final int DEFAULT_HOUR = 8;
    public static final int DEFAULT_MINUTE = 30;
    public static final int DEFAULT_BEFORE = 10;

    private MorningSettingsStore() {}

    private static SharedPreferences pref(Context c) {
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static void setEnabled(Context c, boolean v) {
        pref(c).edit().putBoolean(KEY_ENABLED, v).apply();
    }

    public static boolean isEnabled(Context c) {
        return pref(c).getBoolean(KEY_ENABLED, DEFAULT_ENABLED);
    }

    public static void setTime(Context c, int h, int m) {
        pref(c).edit()
                .putInt(KEY_HOUR, h)
                .putInt(KEY_MINUTE, m)
                .apply();
    }

    public static int getHour(Context c) {
        return pref(c).getInt(KEY_HOUR, DEFAULT_HOUR);
    }

    public static int getMinute(Context c) {
        return pref(c).getInt(KEY_MINUTE, DEFAULT_MINUTE);
    }

    public static void setBefore(Context c, int v) {
        pref(c).edit().putInt(KEY_BEFORE, v).apply();
    }

    public static int getBefore(Context c) {
        return pref(c).getInt(KEY_BEFORE, DEFAULT_BEFORE);
    }
}