package com.lsam.pocketsecretary.core.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class SkinStore {

    private static final String PREF_NAME = "ps_settings";
    private static final String KEY_SKIN_PREFIX = "skin_";

    private SkinStore() {}

    public static void set(Context context,
                           String personaId,
                           String skinId) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        pref.edit()
                .putString(KEY_SKIN_PREFIX + personaId, skinId)
                .apply();
    }

    public static String get(Context context,
                             String personaId,
                             String defaultSkin) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(KEY_SKIN_PREFIX + personaId, defaultSkin);
    }
}
