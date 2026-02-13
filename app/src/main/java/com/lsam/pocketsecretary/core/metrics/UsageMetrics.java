package com.lsam.pocketsecretary.core.metrics;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalDate;

public class UsageMetrics {

    private static final String PREF = "usage_metrics";

    private static final String KEY_LAUNCH = "launch_count";
    private static final String KEY_NOTIFY_SHOW = "notify_show_count";
    private static final String KEY_NOTIFY_TAP = "notify_tap_count";
    private static final String KEY_PERSONA_CHANGE = "persona_change_count";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_STREAK = "streak_days";

    private final SharedPreferences prefs;

    public UsageMetrics(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void recordLaunch() {
        int count = prefs.getInt(KEY_LAUNCH, 0);
        prefs.edit().putInt(KEY_LAUNCH, count + 1).apply();
        updateStreak();
    }

    public void recordNotificationShown() {
        int count = prefs.getInt(KEY_NOTIFY_SHOW, 0);
        prefs.edit().putInt(KEY_NOTIFY_SHOW, count + 1).apply();
    }

    public void recordNotificationTapped() {
        int count = prefs.getInt(KEY_NOTIFY_TAP, 0);
        prefs.edit().putInt(KEY_NOTIFY_TAP, count + 1).apply();
    }

    public void recordPersonaChange() {
        int count = prefs.getInt(KEY_PERSONA_CHANGE, 0);
        prefs.edit().putInt(KEY_PERSONA_CHANGE, count + 1).apply();
    }

    private void updateStreak() {
        String today = LocalDate.now().toString();
        String last = prefs.getString(KEY_LAST_DATE, null);
        int streak = prefs.getInt(KEY_STREAK, 0);

        if (last == null) {
            streak = 1;
        } else if (!last.equals(today)) {
            streak += 1;
        }

        prefs.edit()
                .putString(KEY_LAST_DATE, today)
                .putInt(KEY_STREAK, streak)
                .apply();
    }

    public int getLaunchCount() {
        return prefs.getInt(KEY_LAUNCH, 0);
    }

    public int getStreakDays() {
        return prefs.getInt(KEY_STREAK, 0);
    }
}