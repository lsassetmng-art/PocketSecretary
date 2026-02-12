package com.lsam.pocketsecretary.core.personaos.model;

public enum EmotionState {
    CALM,
    ALERT,
    SPEAKING;

    public static EmotionState fromLegacy(String legacyName) {
        if (legacyName == null) return CALM;
        try { return EmotionState.valueOf(legacyName); }
        catch (Exception e) { return CALM; }
    }
}