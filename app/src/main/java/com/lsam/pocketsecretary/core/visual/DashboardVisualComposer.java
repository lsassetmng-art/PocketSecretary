package com.lsam.pocketsecretary.core.visual;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.lsam.pocketsecretary.R;
import com.lsam.visualruntime.PersonaVisualRuntime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public final class DashboardVisualComposer {

    private DashboardVisualComposer() {}

    public static void tryRender(Activity activity) {

        ImageView iv = activity.findViewById(R.id.avatarView);
        if (iv == null) return;

        String personaId = "persona_local";
        String layersJson = buildStarterLayersJson();

        PersonaVisualRuntime.compose(
                activity.getApplicationContext(),
                personaId,
                layersJson,
                new PersonaVisualRuntime.Callback() {

                    @Override
                    public void onSuccess(File outputPng, boolean fromCache) {
                        if (outputPng == null || !outputPng.exists()) return;

                        activity.runOnUiThread(() ->
                                iv.setImageBitmap(
                                        BitmapFactory.decodeFile(
                                                outputPng.getAbsolutePath()
                                        )
                                )
                        );
                    }

                    @Override
                    public void onError(Exception e) {
                        // UIは絶対にクラッシュさせない
                    }
                }
        );
    }

    private static String buildStarterLayersJson() {
        try {
            JSONObject root = new JSONObject();
            root.put("manifest_sha256", "starter");
            root.put("width", 512);
            root.put("height", 512);

            JSONArray layers = new JSONArray();

            JSONObject layer = new JSONObject();
            layer.put("url", "https://example.com/placeholder.png");
            layer.put("z_index", 0);
            layers.put(layer);

            root.put("layers", layers);
            return root.toString();

        } catch (Exception e) {
            return "{\"manifest_sha256\":\"starter\",\"width\":512,\"height\":512,\"layers\":[]}";
        }
    }
}