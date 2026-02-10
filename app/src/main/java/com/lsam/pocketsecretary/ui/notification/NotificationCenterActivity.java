package com.lsam.pocketsecretary.ui.notification;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.NotificationScheduler;
import com.lsam.pocketsecretary.core.voice.VoiceManager;

/**
 * Phase E: Notification / Voice trigger
 */
public class NotificationCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        setTitle(R.string.notify_title);

        VoiceManager.init(this);

        Button btn = findViewById(R.id.notify_button);
        btn.setOnClickListener(v -> {
            NotificationScheduler.notify(
                    this,
                    getString(R.string.notify_title),
                    "notify.body.sample"
            );
            VoiceManager.speak("notify.voice.sample");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceManager.shutdown();
    }
}