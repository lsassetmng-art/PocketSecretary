package com.lsam.pocketsecretary.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;
import com.lsam.pocketsecretary.persona.SecretaryPersona;

public class NotificationService {

    private static final String CHANNEL_ID = "pocket_secretary_channel";

    private final Context appContext;
    private final SecretaryPersona persona; // null可

    public NotificationService(Context context, SecretaryPersona persona) {
        this.appContext = context.getApplicationContext();
        this.persona = persona;
        createNotificationChannel();
    }

    public void notifyAndRecord(String source, String rawMessage) {

        String title = appContext.getString(R.string.notify_title);
        String text = TextUtils.isEmpty(rawMessage)
                ? appContext.getString(R.string.notify_text_default)
                : rawMessage;

        // Personaがあれば整形
        if (persona != null) {
            text = persona.formatNotification(text);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(appContext, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(appContext)
                .notify(1001, builder.build());

        NotificationHistoryStore.get(appContext)
                .appendAsync(source, title, text);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "PocketSecretaryChannel",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            NotificationManager manager =
                    appContext.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}