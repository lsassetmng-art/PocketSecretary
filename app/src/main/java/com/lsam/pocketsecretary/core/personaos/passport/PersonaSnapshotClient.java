package com.lsam.pocketsecretary.core.personaos.passport;

import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class PersonaSnapshotClient {

    public static final String ISSUE_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/snapshot-issue";

    public static final String VERIFY_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/snapshot-verify";

    private PersonaSnapshotClient() {}

    public static JSONObject issue(String token, JSONObject payload) throws Exception {
        return post(ISSUE_URL, payload, token);
    }

    public static JSONObject verify(JSONObject snapshot) throws Exception {
        JSONObject body = new JSONObject();
        body.put("snapshot", snapshot);
        return post(VERIFY_URL, body, null);
    }

    private static JSONObject post(String urlStr, JSONObject body, String token) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        if (token != null && token.length() > 0) {
            conn.setRequestProperty("x-personaos-issue-token", token);
        }

        OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        os.close();

        InputStream is = conn.getResponseCode() < 400
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return new JSONObject(sb.toString());
    }
}
