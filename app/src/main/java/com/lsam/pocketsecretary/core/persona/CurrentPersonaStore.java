package com.lsam.pocketsecretary.core.persona;

import android.content.Context;
import android.content.SharedPreferences;

public final class CurrentPersonaStore {

    private static final String PREF = "persona_pref";
    private static final String KEY  = "current_persona";

    private CurrentPersonaStore() {}

    public static String get(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY, "alpha");
    }

    public static void set(Context context, String personaId) {
        SharedPreferences sp =
                context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(KEY, personaId).apply();
    }
}