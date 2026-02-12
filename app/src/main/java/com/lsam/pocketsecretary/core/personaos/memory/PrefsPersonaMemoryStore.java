package com.lsam.pocketsecretary.core.personaos.memory;

import android.content.Context;
import android.content.SharedPreferences;

public final class PrefsPersonaMemoryStore implements PersonaMemoryStore {

    private final SharedPreferences prefs;

    public PrefsPersonaMemoryStore(Context context) {
        this.prefs = context.getSharedPreferences("persona_memory_store", Context.MODE_PRIVATE);
    }

    private String key(String personaId, String k) {
        return "p." + personaId + "." + k;
    }

    @Override
    public void bumpOpenedNotification(String personaId) {
        String k = key(personaId, "opened24h");
        prefs.edit().putInt(k, prefs.getInt(k,0)+1).apply();
    }

    @Override
    public void bumpManualTrigger(String personaId) {
        String k = key(personaId, "manual7d");
        prefs.edit().putInt(k, prefs.getInt(k,0)+1).apply();
    }

    @Override
    public void bumpSpeechTool(String personaId) {
        String k = key(personaId, "speech7d");
        prefs.edit().putInt(k, prefs.getInt(k,0)+1).apply();
    }
}