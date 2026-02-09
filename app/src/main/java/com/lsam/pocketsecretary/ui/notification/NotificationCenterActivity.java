package com.lsam.pocketsecretary.ui.notification;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.NotificationScheduler;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class NotificationCenterActivity extends AppCompatActivity {

    private static final int REQ_POST_NOTIF = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        TextView hint = findViewById(R.id.txtNotifyHint);
        hint.setText("Notify — 即時 / 10分後（設定でOFF可）");

        Button btnNow = findViewById(R.id.btnNotifyNow);
        Button btnIn10m = findViewById(R.id.btnNotifyIn10m);

        btnNow.setOnClickListener(v -> ensurePermissionThen(() -> {
            if (!Prefs.isNotifyEnabled(this)) return;
            NotificationScheduler.fireNow(this, "PocketSecretary", "即時通知");
        }));

        btnIn10m.setOnClickListener(v -> ensurePermissionThen(() -> {
            if (!Prefs.isNotifyEnabled(this)) return;
            long at = System.currentTimeMillis() + 10L * 60L * 1000L;
            NotificationScheduler.scheduleAt(this, at, "PocketSecretary", "10分後の通知");
        }));
    }

    private void ensurePermissionThen(Runnable action) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_POST_NOTIF
                );
                return;
            }
        }
        action.run();
    }
}
