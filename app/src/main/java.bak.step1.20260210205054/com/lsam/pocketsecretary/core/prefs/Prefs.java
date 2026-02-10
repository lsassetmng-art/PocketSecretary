package com.lsam.pocketsecretary.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String NAME = "pocket_secretary";

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    // Onboarding
    public static void setOnboarded(Context c, boolean v) {
        sp(c).edit().putBoolean("onboarded", v).apply();
    }

    public static boolean isOnboarded(Context c) {
        return sp(c).getBoolean("onboarded", false);
    }

    // Secretary
    public static void setDefaultSecretary(Context c, String id) {
        sp(c).edit().putString("default_secretary", id).apply();
    }

    public static String getDefaultSecretary(Context c) {
        return sp(c).getString("default_secretary", "hiyori");
    }

    // Scheduled Notification
    public static void saveScheduled(Context c, long at, String title, String text) {
        sp(c).edit()
            .putLong("sched_at", at)
            .putString("sched_title", title)
            .putString("sched_text", text)
            .apply();
    }

    public static long getScheduledAt(Context c) {
        return sp(c).getLong("sched_at", 0L);
    }

    public static String getScheduledTitle(Context c) {
        return sp(c).getString("sched_title", "");
    }

    public static String getScheduledText(Context c) {
        return sp(c).getString("sched_text", "");
    }

    public static void clearScheduled(Context c) {
        sp(c).edit()
            .remove("sched_at")
            .remove("sched_title")
            .remove("sched_text")
            .apply();
    }
}
