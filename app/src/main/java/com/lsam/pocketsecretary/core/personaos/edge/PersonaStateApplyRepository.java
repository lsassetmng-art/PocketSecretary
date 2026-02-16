package com.lsam.pocketsecretary.core.personaos.edge;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PersonaStateApplyRepository {

    private static final ExecutorService EXEC =
            Executors.newSingleThreadExecutor();

    private PersonaStateApplyRepository(){}

    public static void applyEvent(
            Context ctx,
            String personaId,
            String userId,
            String eventType
    ) {

        EXEC.execute(() -> {
            try {

                JSONObject payload = new JSONObject();
                payload.put("timestamp",
                        System.currentTimeMillis());

                PersonaStateApplyResponse r =
                        PersonaStateApplyClient.run(
                                ctx,
                                personaId,
                                userId,
                                eventType,
                                payload
                        );

                if (!r.success) {
                    Log.w("PersonaStateApply",
                            "failed: " + r.error);
                }

            } catch (Throwable t) {
                Log.w("PersonaStateApply",
                        "error", t);
            }
        });
    }
}
