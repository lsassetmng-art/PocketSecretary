package com.lsam.pocketsecretary.core.visual;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.lsam.pocketsecretary.R;
import com.lsam.visualruntime.ComposeRequest;
import com.lsam.visualruntime.PersonaVisualRuntime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public final class DashboardVisualComposer {

    private DashboardVisualComposer() {}

    public static void tryRender(Activity activity) {
        ImageView iv = activity.findViewById(R.id.personaImage);
        if (iv == null) return;

        // TODO: Replace with Edge/local layersJson provider.
        // This is a minimal starter payload that compiles and is safe.
        String layersJson = buildStarterLayersJson();

        ComposeRequest req = new ComposeRequest(
                "persona_local",
                layersJson,
                512,
                512,
                "dash_persona"
        );

        PersonaVisualRuntime.get().composeAsync(activity.getApplicationContext(), req,
                new com.lsam.visualruntime.ComposeCallback() {
                    @Override
                    public void onSuccess(com.lsam.visualruntime.ComposeResult result) {
                        File f = result.outputFile;
                        if (f == null || !f.exists()) return;
                        activity.runOnUiThread(() ->
                                iv.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()))
                        );
                    }

                    @Override
                    public void onError(Exception e) {
                        // no-op: never crash UI
                    }
                });
    }

    private static String buildStarterLayersJson() {
        try {
            JSONObject root = new JSONObject();
            root.put("manifest_sha256", "starter");
            root.put("width", 512);
            root.put("height", 512);

            JSONArray layers = new JSONArray();

            // NOTE: Keep https-only contract. This is a harmless placeholder.
            // Replace with real https assets when ready.
            // If unreachable offline, runtime will fallback gracefully (no crash).
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
