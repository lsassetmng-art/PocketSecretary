package com.lsam.pocketsecretary.core.personaos;

import android.content.Context;

import java.time.Instant;
import java.util.UUID;

import com.lsam.pocketsecretary.core.personaos.memory.PersonaMemoryStore;
import com.lsam.pocketsecretary.core.personaos.memory.PrefsPersonaMemoryStore;
import com.lsam.pocketsecretary.core.personaos.model.*;
import com.lsam.pocketsecretary.core.personaos.provider.LocalPersonaProvider;
import com.lsam.pocketsecretary.core.personaos.provider.PersonaProvider;

public final class PersonaEngine {

    private static volatile PersonaProvider provider;
    private static volatile PersonaMemoryStore memoryStore;

    private PersonaEngine(){}

    public static void init(Context context) {
        if (provider == null) {
            synchronized (PersonaEngine.class) {
                if (provider == null) {
                    provider = new LocalPersonaProvider(context.getApplicationContext());
                }
            }
        }
        if (memoryStore == null) {
            synchronized (PersonaEngine.class) {
                if (memoryStore == null) {
                    memoryStore = new PrefsPersonaMemoryStore(context.getApplicationContext());
                }
            }
        }
    }

    public static void setProvider(PersonaProvider p) { provider = p; }

    public static PersonaMemoryStore memory(Context context) {
        init(context);
        return memoryStore;
    }

    public static PersonaResponse generate(
            Context context,
            String personaId,
            EmotionState emotionState,
            PersonaChannel channel,
            String summary,
            String timeIso,
            Integer todayCount
    ) {
        init(context);

        String reqId = UUID.randomUUID().toString();
        String nowIso = Instant.now().toString();

        PersonaContext ctx = new PersonaContext(
                reqId,
                "PocketSecretary",
                (personaId != null && !personaId.trim().isEmpty()) ? personaId : "default",
                "ja-JP",
                "Asia/Tokyo",
                nowIso,
                (emotionState != null) ? emotionState : EmotionState.CALM,
                channel,
                summary,
                timeIso,
                todayCount
        );

        PersonaRequest req = new PersonaRequest(ctx, PersonaRequest.Constraints.defaults());
        return provider.generate(req);
    }

    /**
     * Phase G:
     * Explicit provider override (design only).
     * Default remains LocalPersonaProvider.
     */
    public static void useRemoteProvider(
            com.lsam.pocketsecretary.core.personaos.provider.PersonaProvider remoteProvider
    ) {
        provider = remoteProvider;
    }

}