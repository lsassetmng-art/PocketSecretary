package com.lsam.pocketsecretary.core.personaos.edge;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class PersonaStateApplyClient {

    private static final int TIMEOUT = 10000;

    private PersonaStateApplyClient(){}

    public static PersonaStateApplyResponse run(
            Context ctx,
            String personaId,
            String userId,
            String eventType,
            JSONObject payload
    ) throws Exception {

        String project = ctx.getString(
                ctx.getResources().getIdentifier(
                        "ps_supabase_project_ref",
                        "string",
                        ctx.getPackageName()
                )
        );

        String anon = ctx.getString(
                ctx.getResources().getIdentifier(
                        "ps_supabase_anon_key",
                        "string",
                        ctx.getPackageName()
                )
        );

        String urlStr =
                "https://" + project +
                ".supabase.co/functions/v1/persona-state-apply";

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Authorization", "Bearer " + anon);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject body = new JSONObject();
            body.put("persona_id", personaId);
            body.put("user_id", userId);
            body.put("source", "business.pocketsecretary");
            body.put("event_type", eventType);
            body.put("payload", payload);

            byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);

            BufferedOutputStream os =
                    new BufferedOutputStream(conn.getOutputStream());
            os.write(bytes);
            os.flush();
            os.close();

            int code = conn.getResponseCode();
            InputStream is =
                    (code >= 200 && code < 300)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

            String text = readAll(is);

            JSONObject json = new JSONObject(text);
            boolean success = json.optBoolean("success", false);

            if (!success) {
                return PersonaStateApplyResponse.failure(
                        json.optString("error", "unknown error")
                );
            }

            int rev = json.optInt("persona_state_revision", 0);
            String job = json.optString("next_visual_job_id", null);

            return PersonaStateApplyResponse.success(rev, job);

        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String readAll(InputStream is) throws Exception {
        if (is == null) return "";
        BufferedReader r =
                new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) b.append(line);
        r.close();
        return b.toString();
    }
}
