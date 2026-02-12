package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import com.lsam.pocketsecretary.persona.EmotionStateStore;

import java.util.Map;

public final class PersonaEmotionApplier {

    private PersonaEmotionApplier() {}

    public static void applyBaseEmotion(Context context, String personaId) {

        Map<String, String> map =
                PersonaYamlLoader.load(context, personaId);

        String base = map.get("emotion_base");

        if (base == null) return;

        try {
            EmotionStateStore.Emotion emotion =
                    EmotionStateStore.Emotion.valueOf(base);

            EmotionStateStore.getInstance().set(emotion);

        } catch (Exception ignored) {
        }
    }
}