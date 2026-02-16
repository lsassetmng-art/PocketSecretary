package com.lsam.pocketsecretary.core;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseManager {

    private static final String TAG = "SupabaseManager";

    private static final String SUPABASE_URL =
            "https://ihrukfdlcolygyvccujd.supabase.co";

    // ⚠ anon public keyのみ（service_role絶対禁止）
    private static final String SUPABASE_ANON_KEY =
            "YOUR_ANON_PUBLIC_KEY";

    private static String accessToken;

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 匿名ログイン
     */
    public static void signInAnonymously() {

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/token?grant_type=anonymous")
                .addHeader("apikey", SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Anonymous login failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Login failed: " + response.code());
                    return;
                }

                String body = response.body().string();

                try {
                    JSONObject json = new JSONObject(body);
                    accessToken = json.getString("access_token");
                    Log.d(TAG, "Anonymous login success");
                } catch (Exception e) {
                    Log.e(TAG, "Token parse error", e);
                }
            }
        });
    }

    /**
     * AccessToken取得
     */
    public static String getAccessToken() {
        return accessToken;
    }

    /**
     * ログアウト（ローカルのみ）
     */
    public static void signOut() {
        accessToken = null;
    }
}
