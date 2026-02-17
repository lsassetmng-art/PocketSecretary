package com.lsam.pocketsecretary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.ui.settings.SettingsActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int REQ_NOTIFICATION_PERMISSION = 1001;

    private FrameLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_base);

        contentContainer = findViewById(R.id.baseContentContainer);

        TextView title = findViewById(R.id.txtHeaderTitle);
        if (title != null) {
            title.setText(getHeaderTitle());
        }

        View settings = findViewById(R.id.headerSettings);
        if (settings != null) {

            if (showSettingsButton()) {
                settings.setVisibility(View.VISIBLE);
                settings.setOnClickListener(v ->
                        startActivity(new Intent(this, SettingsActivity.class))
                );
            } else {
                settings.setVisibility(View.GONE);
            }
        }

        applyEdgePadding();

        // ✅ Additive: Android13+ notification permission
        ensureNotificationPermission();
    }

    protected void setBaseContent(int layoutRes) {
        LayoutInflater.from(this)
                .inflate(layoutRes, contentContainer, true);
    }

    protected abstract String getHeaderTitle();

    protected boolean showSettingsButton() {
        return true;
    }

    protected void applyEdgePadding() {
        View header = findViewById(R.id.commonHeaderRoot);
        if (header != null) {
            header.setPadding(
                    header.getPaddingLeft(),
                    header.getPaddingTop() + getStatusBarHeight(),
                    header.getPaddingRight(),
                    header.getPaddingBottom()
            );
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // =========================================================
    // Notification Permission (Android 13+)
    // =========================================================

    private void ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIFICATION_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_NOTIFICATION_PERMISSION) {
            // No-op (permission result handled passively)
        }
    }
}
