package com.lsam.pocketsecretary.core.background;

import android.content.Context;

import com.lsam.pocketsecretary.core.assets.AssetRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BackgroundMetaLoader {

    public static class BackgroundMeta {
        public String backgroundId;
        public List<String> includes;
        public boolean isPremium;
        public int assetVersion;
    }

    public static BackgroundMeta load(Context context, String backgroundId) {

        try {

            String path = "background/" + backgroundId + "/meta.json";
            String jsonText = AssetRepository.loadText(context, path);

            if (jsonText == null) return null;

            JSONObject obj = new JSONObject(jsonText);

            BackgroundMeta meta = new BackgroundMeta();
            meta.backgroundId = obj.optString("background_id", backgroundId);
            meta.isPremium = obj.optBoolean("is_premium", false);
            meta.assetVersion = obj.optInt("asset_version", 1);

            JSONArray arr = obj.optJSONArray("includes");
            meta.includes = new ArrayList<>();

            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    meta.includes.add(arr.optString(i));
                }
            }

            return meta;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
