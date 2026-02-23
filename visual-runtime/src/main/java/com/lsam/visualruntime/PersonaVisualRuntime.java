package com.lsam.visualruntime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonaVisualRuntime {

    private static volatile PersonaVisualRuntime instance;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static PersonaVisualRuntime get() {
        if (instance == null) {
            synchronized (PersonaVisualRuntime.class) {
                if (instance == null) instance = new PersonaVisualRuntime();
            }
        }
        return instance;
    }

    public void composeAsync(Context context, ComposeRequest request, ComposeCallback callback) {
        executor.execute(() -> {
            try {
                ComposeResult r = composeInternal(context, request);
                callback.onSuccess(r);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    private ComposeResult composeInternal(Context context, ComposeRequest request) throws Exception {
        JSONObject root = new JSONObject(request.layersJson);

        String manifestSha256 = root.getString("manifest_sha256");
        int mw = root.getInt("width");
        int mh = root.getInt("height");

        File cacheDir = new File(context.getFilesDir(), "persona-cache");
        if (!cacheDir.exists()) cacheDir.mkdirs();

        // Cache key = sha256(manifest_json_string)
        String key = sha256Hex(request.layersJson);
        File outFile = new File(cacheDir, request.outputName + "_" + key + ".png");

        if (outFile.exists() && outFile.length() > 0) {
            return new ComposeResult(outFile, manifestSha256, true);
        }

        JSONArray layers = root.getJSONArray("layers");

        Bitmap base = Bitmap.createBitmap(mw, mh, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(base);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        for (int i = 0; i < layers.length(); i++) {
            JSONObject layer = layers.getJSONObject(i);
            String url = layer.getString("url");
            validateHttpsUrl(url);

            float alpha = 1.0f;
            if (layer.has("alpha")) {
                alpha = (float) layer.optDouble("alpha", 1.0);
                if (alpha < 0f) alpha = 0f;
                if (alpha > 1f) alpha = 1f;
            }
            paint.setAlpha((int) (alpha * 255f));

            Bitmap bmp = downloadBitmap(url);
            if (bmp != null) {
                canvas.drawBitmap(bmp, 0f, 0f, paint);
                bmp.recycle();
            } else {
                // Missing layer: continue (fallback policy)
            }
        }

        // Size override (request width/height) if needed
        Bitmap finalBmp = base;
        if (request.width > 0 && request.height > 0
                && (request.width != mw || request.height != mh)) {
            finalBmp = Bitmap.createScaledBitmap(base, request.width, request.height, true);
            base.recycle();
        }

        FileOutputStream fos = new FileOutputStream(outFile);
        try {
            finalBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } finally {
            fos.close();
            finalBmp.recycle();
        }

        return new ComposeResult(outFile, manifestSha256, false);
    }

    private static void validateHttpsUrl(String url) {
        String u = url == null ? "" : url.toLowerCase(Locale.US);
        if (!u.startsWith("https://")) {
            throw new IllegalArgumentException("invalid url scheme");
        }
    }

    private static Bitmap downloadBitmap(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestMethod("GET");
            conn.connect();

            int code = conn.getResponseCode();
            if (code >= 400) return null;

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            return BitmapFactory.decodeStream(bis);
        } catch (Exception ignore) {
            return null;
        } finally {
            try { if (conn != null) conn.disconnect(); } catch (Exception ignored) {}
        }
    }

    private static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : d) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
