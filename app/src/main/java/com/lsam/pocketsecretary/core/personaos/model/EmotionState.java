package com.lsam.pocketsecretary.core.personaos.model;

public enum EmotionState {

    // --- Existing (do not remove / do not reorder) ---
    CALM,
    ALERT,
    SPEAKING,

    // --- Phase H additive extensions ---
    CALM_SOFTENED,
    SLIGHTLY_SOFTENED,

    FRIENDLY_OPEN,
    SLIGHTLY_WARM,
    WARM_POSITIVE,

    CONCERNED_SOFT,
    FIRM_GENTLE;

    /**
     * Backward compatible parser.
     * Accepts legacy names, lowercase, hyphen, space.
     */
    public static EmotionState fromLegacy(String legacyName) {

        if (legacyName == null || legacyName.trim().isEmpty()) {
            return CALM;
        }

        try {
            String normalized = legacyName
                    .trim()
                    .toUpperCase()
                    .replace('-', '_')
                    .replace(' ', '_');

            return EmotionState.valueOf(normalized);

        } catch (Exception ignored) {
            return CALM;
        }
    }
}
