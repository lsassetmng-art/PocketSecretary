package com.lsam.pocketsecretary.ui.dashboard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.visualruntime.anim.RigFrameRenderEngine;

public class DashboardActivity extends BaseActivity {

    private RigFrameRenderEngine avatarEngine;
    private ImageView avatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_dashboard);

        avatarView = findViewById(R.id.avatarView);

        String personaId = "persona_local";
        String layersJson = buildResidentLayersJson();

        avatarEngine = new RigFrameRenderEngine(
                getApplicationContext(),
                personaId,
                24,
                layersJson,
                new RigFrameRenderEngine.Listener() {
                    @Override
                    public void onBitmap(long frameIndex, @NonNull Bitmap bitmap) {
                        runOnUiThread(() -> avatarView.setImageBitmap(bitmap));
                    }

                    @Override
                    public void onError(@NonNull Throwable error) {
                        // 常駐型のためクラッシュ禁止
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (avatarEngine != null) avatarEngine.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (avatarEngine != null) avatarEngine.stop();
    }

    private String buildResidentLayersJson() {
        return "{\n" +
                "  \"manifest_sha256\": \"resident\",\n" +
                "  \"width\": 768,\n" +
                "  \"height\": 768,\n" +
                "  \"layers\": []\n" +
                "}";
    }

    @Override
    protected String getHeaderTitle() {
        return "Dashboard";
    }

    @Override
    protected boolean showSettingsButton() {
        return true;
    }
}
