package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import com.lsam.pocketsecretary.core.emotion.EmotionEngine;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.persona.EmotionStateStore;

import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;

/**
 * Phase F:
 * - Keep API signature (additive-only friendly)
 * - No Japanese literals here
 * - Delegate morning message to PersonaEngine
 */
public class PersonaToneProvider {

    public static String buildMorningMessage(
            Context context,
            String persona,
            EmotionEngine.Emotion emotion,
            int todayCount
    ) {
        // personaId: prefer argument, fallback to CurrentPersonaStore
        String personaId = (persona != null && !persona.trim().isEmpty())
                ? persona
                : CurrentPersonaStore.get(context);

        EmotionState mapped = mapEmotion(emotion);

        return PersonaEngine.generate(
                context,
                personaId,
                mapped,
                PersonaChannel.MORNING,
                null,
                null,
                todayCount
        ).text;
    }

    private static EmotionState mapEmotion(EmotionEngine.Emotion e) {
        if (e == null) return EmotionState.CALM;
        switch (e) {
            case ALERT: return EmotionState.ALERT;
            case SPEAKING: return EmotionState.SPEAKING;
            default: return EmotionState.CALM;
        }
    }
}