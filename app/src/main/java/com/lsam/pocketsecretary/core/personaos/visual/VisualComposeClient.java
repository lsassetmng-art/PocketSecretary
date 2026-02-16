package com.lsam.pocketsecretary.core.personaos.visual;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class VisualComposeClient {

    private static final int TIMEOUT_MS = 10000;

    private VisualComposeClient(){}

    public static VisualComposeResponse run(Context ctx, String jobId) throws Exception {

        if (jobId == null || jobId.trim().isEmpty()) {
            return VisualComposeResponse.failure("job_id empty");
        }

        // ✅ 修正ポイント
        VisualComposeConfig cfg = VisualComposeConfig.from(ctx);

        String endpoint =
                "https://" +
                cfg.projectRef +
                ".supabase.co/" +
                cfg.functionPath;

        HttpURLConnection conn = null;

        try {
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Authorization", "Bearer " + cfg.anonKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject body = new JSONObject();
            body.put("job_id", jobId);

            byte[] payload = body.toString().getBytes(StandardCharsets.UTF_8);
            BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(payload);
            os.flush();
            os.close();

            int code = conn.getResponseCode();
            InputStream is =
                    (code >= 200 && code < 300)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

            String responseText = readAll(is);
            JSONObject json = new JSONObject(responseText);

            if (!json.optBoolean("success", false)) {
                return VisualComposeResponse.failure(
                        json.optString("error", "unknown error")
                );
            }

            String outputPath = json.optString("output_path", "");
            if (outputPath.isEmpty()) {
                return VisualComposeResponse.failure("output_path missing");
            }

            return VisualComposeResponse.success(jobId, outputPath);

        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String readAll(InputStream is) throws Exception {
        if (is == null) return "";
        BufferedReader r = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            b.append(line);
        }
        r.close();
        return b.toString();
    }
}
