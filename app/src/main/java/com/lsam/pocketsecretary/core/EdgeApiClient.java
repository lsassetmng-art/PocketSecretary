package com.lsam.pocketsecretary.core;

import okhttp3.*;
import java.io.IOException;

public class EdgeApiClient {

    private static final String BASE_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/";

    private static final OkHttpClient client = new OkHttpClient();

    public static void applyPersonaEvent(
            String accessToken,
            String personaId,
            String eventType,
            Callback callback
    ) {

        String json = "{"
                + "\"persona_id\":\"" + personaId + "\","
                + "\"event_type\":\"" + eventType + "\""
                + "}";

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "persona-state-apply")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
