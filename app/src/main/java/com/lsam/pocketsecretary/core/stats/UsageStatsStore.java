package com.lsam.pocketsecretary.core.stats;

import android.content.Context;
import android.content.SharedPreferences;

public class UsageStatsStore {

    private static final String PREF = "usage_stats";
    private static final String KEY_ALERT_COUNT = "alert_count_hour";
    private static final String KEY_LAST_HOUR = "last_hour";

    private final SharedPreferences prefs;

    public UsageStatsStore(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void recordAlert() {
        long hour = System.currentTimeMillis() / 3600000L;
        long stored = prefs.getLong(KEY_LAST_HOUR, -1);

        if (stored != hour) {
            prefs.edit()
                    .putLong(KEY_LAST_HOUR, hour)
                    .putInt(KEY_ALERT_COUNT, 1)
                    .apply();
        } else {
            int count = prefs.getInt(KEY_ALERT_COUNT, 0);
            prefs.edit().putInt(KEY_ALERT_COUNT, count + 1).apply();
        }
    }

    public int getAlertCountThisHour() {
        long hour = System.currentTimeMillis() / 3600000L;
        long stored = prefs.getLong(KEY_LAST_HOUR, -1);
        if (stored != hour) return 0;
        return prefs.getInt(KEY_ALERT_COUNT, 0);
    }
}
