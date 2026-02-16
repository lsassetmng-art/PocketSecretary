package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class StageStore {

    private static final String PREF = "ps_stage_store";
    private static final String KEY_PREFIX = "stage_";

    public static String getStage(Context ctx, String personaId) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY_PREFIX + personaId, "s0");
    }

    public static void setStage(Context ctx, String personaId, String stageId) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PREFIX + personaId, stageId).apply();
    }
}
