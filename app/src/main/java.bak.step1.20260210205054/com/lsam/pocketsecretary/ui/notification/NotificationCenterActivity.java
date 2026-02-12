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
        hint.setText("Notify 驕ｯ・ｶ郢晢ｽｻ髯ｷ螂・ｽｽ・ｳ髫ｴ蠑ｱ繝ｻ/ 10髯具ｽｻ郢晢ｽｻ繝ｻ・ｾ鬲・ｼ夲ｽｽ・ｼ鬩帙・・ｽ・ｨ繝ｻ・ｭ髯橸ｽｳ陞｢・ｹ邵ｲ荳ｹFF髯ｷ・ｿ繝ｻ・ｯ郢晢ｽｻ郢晢ｽｻ);

        Button btnNow = findViewById(R.id.btnNotifyNow);
        Button btnIn10m = findViewById(R.id.btnNotifyIn10m);

        btnNow.setOnClickListener(v -> ensurePermissionThen(() -> {
            if (!Prefs.isNotifyEnabled(this)) return;
            NotificationScheduler.fireNow(this, "PocketSecretary", "髯ｷ螂・ｽｽ・ｳ髫ｴ蠑ｱ・具ｾつ陞溘ｊ・｡繝ｻ);
        }));

        btnIn10m.setOnClickListener(v -> ensurePermissionThen(() -> {
            if (!Prefs.isNotifyEnabled(this)) return;
            long at = System.currentTimeMillis() + 10L * 60L * 1000L;
            NotificationScheduler.scheduleAt(this, at, "PocketSecretary", "10髯具ｽｻ郢晢ｽｻ繝ｻ・ｾ陟募ｾ後・鬯ｨ・ｾ陞溘ｊ・｡繝ｻ);
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
