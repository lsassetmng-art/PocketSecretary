package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationSettingsStore {

    private static final String PREF = "ps_notification_settings";

    private static final String K_MORNING_ENABLED = "morning_enabled";
    private static final String K_MORNING_HOUR = "morning_hour";
    private static final String K_MORNING_MIN = "morning_minute";
    private static final String K_REMINDER_BEFORE_MIN = "reminder_before_minutes";

    public static final boolean DEF_MORNING_ENABLED = true;
    public static final int DEF_MORNING_HOUR = 8;
    public static final int DEF_MORNING_MIN = 0;
    public static final int DEF_REMINDER_BEFORE_MIN = 30;

    private static SharedPreferences sp(Context c) {
        return c.getApplicationContext()
                .getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static boolean isMorningEnabled(Context c) {
        return sp(c).getBoolean(K_MORNING_ENABLED, DEF_MORNING_ENABLED);
    }

    public static void setMorningEnabled(Context c, boolean enabled) {
        sp(c).edit().putBoolean(K_MORNING_ENABLED, enabled).apply();
    }

    public static int getMorningHour(Context c) {
        return sp(c).getInt(K_MORNING_HOUR, DEF_MORNING_HOUR);
    }

    public static int getMorningMinute(Context c) {
        return sp(c).getInt(K_MORNING_MIN, DEF_MORNING_MIN);
    }

    public static void setMorningTime(Context c, int hour, int minute) {
        sp(c).edit()
                .putInt(K_MORNING_HOUR, hour)
                .putInt(K_MORNING_MIN, minute)
                .apply();
    }

    public static int getReminderBeforeMinutes(Context c) {
        int v = sp(c).getInt(K_REMINDER_BEFORE_MIN, DEF_REMINDER_BEFORE_MIN);
        if (v < 5) v = 5;
        if (v > 180) v = 180;
        return v;
    }

    public static void setReminderBeforeMinutes(Context c, int minutes) {
        if (minutes < 5) minutes = 5;
        if (minutes > 180) minutes = 180;
        sp(c).edit().putInt(K_REMINDER_BEFORE_MIN, minutes).apply();
    }
}
