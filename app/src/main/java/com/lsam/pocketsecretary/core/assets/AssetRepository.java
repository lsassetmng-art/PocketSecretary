package com.lsam.pocketsecretary.core.assets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetRepository {

    // Canonical v1.1: assets/runtime/ 以下のみ参照
    private static final String RUNTIME_BASE = "runtime/";

    // ==========================
    // 共通Bitmap読み込み（runtime固定）
    // ==========================
    public static Bitmap loadBitmap(Context context, String relativePath) {
        try {
            String path = normalizeRuntimePath(relativePath);
            InputStream is = context.getAssets().open(path);
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================
    // テキスト読み込み（json/yaml）（runtime固定）
    // ==========================
    public static String loadText(Context context, String relativePath) {
        try {
            String path = normalizeRuntimePath(relativePath);
            InputStream is = context.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================
    // exists（runtime固定）
    // ==========================
    public static boolean exists(Context context, String relativePath) {
        try {
            String path = normalizeRuntimePath(relativePath);
            InputStream is = context.getAssets().open(path);
            is.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String normalizeRuntimePath(String path) {
        if (path == null) return RUNTIME_BASE;
        if (path.startsWith(RUNTIME_BASE)) return path;   // "runtime/..."
        // それ以外は "persona/..." 等を許し、内部で runtime/ を付ける
        return RUNTIME_BASE + path;
    }

    // =========================================================
    // Canonical v1.1 Path Builders（runtime/ 配下）
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
        // type = morning / day / night
        return "background/" + backgroundPackId + "/" + type + ".png";
    }
}
