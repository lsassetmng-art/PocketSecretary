package com.lsam.pocketsecretary.core.assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AssetRepository {

    // Canonical v1.2
    // assets/runtime/ 以下のみ参照
    private static final String TAG = "AssetRepository";
    private static final String RUNTIME_BASE = "runtime/";

    // ==========================
    // 共通Bitmap読み込み（runtime固定）
    // ==========================
    public static Bitmap loadBitmap(Context context, String relativePath) {
        InputStream is = null;
        String normalized = null;

        try {
            normalized = normalizeRuntimePath(relativePath);
            is = context.getAssets().open(normalized);
            Bitmap bmp = BitmapFactory.decodeStream(is);

            if (bmp == null) {
                Log.w(TAG, "Bitmap decode returned null: rel=" + relativePath + " norm=" + normalized);
            }
            return bmp;

        } catch (Exception e) {
            Log.w(TAG, "Bitmap load failed: rel=" + relativePath + " norm=" + normalized, e);
            return null;

        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
    }

    // ==========================
    // テキスト読み込み（json/yaml）（runtime固定）
    // ==========================
    public static String loadText(Context context, String relativePath) {
        InputStream is = null;
        BufferedReader reader = null;
        String normalized = null;

        try {
            normalized = normalizeRuntimePath(relativePath);
            is = context.getAssets().open(normalized);
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();

        } catch (Exception e) {
            Log.w(TAG, "Text load failed: rel=" + relativePath + " norm=" + normalized, e);
            return null;

        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ignored) {}
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
    }

    // ==========================
    // exists（runtime固定）
    // ==========================
    public static boolean exists(Context context, String relativePath) {
        InputStream is = null;
        String normalized = null;

        try {
            normalized = normalizeRuntimePath(relativePath);
            is = context.getAssets().open(normalized);
            return true;

        } catch (Exception ignored) {
            return false;

        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
    }

    private static String normalizeRuntimePath(String path) {
        if (path == null || path.isEmpty()) return RUNTIME_BASE;
        if (path.startsWith(RUNTIME_BASE)) return path;
        return RUNTIME_BASE + path;
    }

    // =========================================================
    // Canonical Path Builders（runtime/ 配下）
    // =========================================================

    // ---------- PersonaPack ----------
    public static String personaManifest(String personaId) {
        return "persona/" + personaId + "/manifest.json";
    }

    public static String personaYaml(String personaId) {
        return "persona/" + personaId + "/persona.yaml";
    }

    // ---------- VisualPack ----------
    public static String visualManifest(String visualPackId) {
        return "visual/" + visualPackId + "/manifest.json";
    }

    public static String visualSkinDir(String visualPackId, String skinId) {
        return "visual/" + visualPackId + "/skins/" + skinId + "/";
    }

    public static String visualSkinImage(String visualPackId, String skinId, String fileName) {
        return visualSkinDir(visualPackId, skinId) + fileName;
    }

    // ---------- BackgroundPack ----------
    public static String backgroundManifest(String backgroundPackId) {
        return "background/" + backgroundPackId + "/manifest.json";
    }

    public static String backgroundImage(String backgroundPackId, String type) {
        if (backgroundPackId == null || backgroundPackId.isEmpty()) return "background/desk_set_001/day.png";
        if (type == null || type.isEmpty()) type = "day";
        // type = morning / day / night
        return "background/" + backgroundPackId + "/" + type + ".png";
    }

    // （任意の診断用）assets/runtime/... を list したい時に使える
    public static String[] list(Context ctx, String relativeDir) {
        try {
            String dir = normalizeRuntimePath(relativeDir);
            AssetManager am = ctx.getAssets();
            return am.list(dir);
        } catch (Throwable t) {
            Log.w(TAG, "Asset list failed: " + relativeDir, t);
            return null;
        }
    }
}
