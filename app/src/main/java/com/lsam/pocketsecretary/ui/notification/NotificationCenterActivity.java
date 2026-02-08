package com.lsam.pocketsecretary.ui.notification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.NotificationUtil;

public class NotificationCenterActivity extends AppCompatActivity {

    private static final int REQ_POST_NOTIF = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        NotificationUtil.ensureChannel(this);

        TextView hint = findViewById(R.id.txtNotifyHint);
        hint.setText("Notify — 即時通知 / 10秒後通知（READ ONLY）");

        Button btnNow = findViewById(R.id.btnNotifyNow);
        Button btnIn10 = findViewById(R.id.btnNotifyIn10);

        btnNow.setOnClickListener(v -> ensurePermissionThen(() -> fireNow("即時通知", "ボタンで発火しました")));
        btnIn10.setOnClickListener(v -> ensurePermissionThen(() -> scheduleIn10s("予約通知", "10秒後に通知します")));
    }

    private void ensurePermissionThen(Runnable action) {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_POST_NOTIF);
                return;
            }
        }
        action.run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 許可後はユーザーがもう一度ボタンを押す運用（勝手に通知しない）
    }

    private void fireNow(String title, String text) {
        Intent i = new Intent(this, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, text);
        sendBroadcast(i);
    }

    private void scheduleIn10s(String title, String text) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent(this, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, text);

        PendingIntent pi = PendingIntent.getBroadcast(
                this,
                1,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        long triggerAt = SystemClock.elapsedRealtime() + 10_000L;
        if (am != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                am.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pi);
            } else {
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pi);
            }
        }
    }
}
