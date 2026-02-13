package com.lsam.pocketsecretary.core.stats;

import android.content.Context;
import android.content.SharedPreferences;

public class EngagementTracker {

    private static final String PREF = "engagement_store";
    private static final String KEY_SCORE = "engagement_score";

    private final SharedPreferences prefs;

    public EngagementTracker(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void recordInteraction() {
        int score = prefs.getInt(KEY_SCORE, 0);
        prefs.edit().putInt(KEY_SCORE, score + 1).apply();
    }

    public int getScore() {
        return prefs.getInt(KEY_SCORE, 0);
    }
}
