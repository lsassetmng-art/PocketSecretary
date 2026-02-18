package com.lsam.pocketsecretary.core.theme;

import android.content.Context;
import android.content.SharedPreferences;

public final class PersonaProvider {

    private static final String PREFS = "ps_prefs";
    private static final String KEY_PERSONA_ID = "current_persona_id";
    private static final String KEY_TONE = "current_persona_tone";
    private static final String KEY_ACCENT = "current_persona_accent";

    private PersonaProvider() {}

    public static String getPersonaId(Context context) {
        return prefs(context).getString(KEY_PERSONA_ID, "default");
    }

    public static String getToneType(Context context) {
        return prefs(context).getString(KEY_TONE, "tone_balanced");
    }

    public static int getAccentColor(Context context) {
        // Stored as ARGB int. Default: muted blue.
        return prefs(context).getInt(KEY_ACCENT, 0xFF2A3F5F);
    }

    public static void setPersona(Context context, String personaId, String toneType, int accentColor) {
        prefs(context).edit()
                .putString(KEY_PERSONA_ID, personaId)
                .putString(KEY_TONE, toneType)
                .putInt(KEY_ACCENT, accentColor)
                .apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
