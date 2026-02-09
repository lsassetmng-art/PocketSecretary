package com.lsam.pocketsecretary.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String FILE = "pocketsecretary_prefs";
    private static final String KEY_NOTIFY_ENABLED = "notify_enabled";
    private static final String KEY_DEFAULT_SECRETARY = "default_secretary";
    private static final String KEY_SCHEDULED_AT = "scheduled_at";
    private static final String KEY_SCHEDULED_TITLE = "scheduled_title";
    private static final String KEY_SCHEDULED_TEXT = "scheduled_text";

    public static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static boolean isNotifyEnabled(Context c) {
        return sp(c).getBoolean(KEY_NOTIFY_ENABLED, true);
    }

    public static void setNotifyEnabled(Context c, boolean v) {
        sp(c).edit().putBoolean(KEY_NOTIFY_ENABLED, v).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return sp(c).getString(KEY_DEFAULT_SECRETARY, "hiyori");
    }

    public static void setDefaultSecretary(Context c, String id) {
        sp(c).edit().putString(KEY_DEFAULT_SECRETARY, id).apply();
    }

    public static void saveScheduled(Context c, long atMillis, String title, String text) {
        sp(c).edit()
                .putLong(KEY_SCHEDULED_AT, atMillis)
                .putString(KEY_SCHEDULED_TITLE, title)
                .putString(KEY_SCHEDULED_TEXT, text)
                .apply();
    }

    public static long getScheduledAt(Context c) {
        return sp(c).getLong(KEY_SCHEDULED_AT, -1L);
    }

    public static String getScheduledTitle(Context c) {
        return sp(c).getString(KEY_SCHEDULED_TITLE, null);
    }

    public static String getScheduledText(Context c) {
        return sp(c).getString(KEY_SCHEDULED_TEXT, null);
    }

    public static void clearScheduled(Context c) {
        sp(c).edit()
                .remove(KEY_SCHEDULED_AT)
                .remove(KEY_SCHEDULED_TITLE)
                .remove(KEY_SCHEDULED_TEXT)
                .apply();
    }
}
