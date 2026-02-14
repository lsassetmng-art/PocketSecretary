package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

public class VoiceConfigLoader {

    public static class VoiceConfig {
        public String engine;
        public String locale;
        public float pitch;
        public float speechRate;
        public String style;
    }

    public static VoiceConfig load(Context context,
                                   String personaId,
                                   String voiceId) {

        // Phase G-1:
        // Voice system temporarily disabled.
        // Will be reintroduced in Phase H with runtime/voice structure.

        return null;
    }
}
