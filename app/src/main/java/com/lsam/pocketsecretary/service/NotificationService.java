package com.lsam.pocketsecretary.service;

import com.lsam.pocketsecretary.persona.EmotionStateStore;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lsam.pocketsecretary.history.NotificationHistoryStore;
import com.lsam.pocketsecretary.persona.PersonaRegistry;
import com.lsam.pocketsecretary.persona.SecretaryPersona;

public class NotificationService {

    private static final String CHANNEL_ID = "pocket_secretary_channel";
    private final Context context;

    public NotificationService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void notifyAndRecord(String source, String message) {
        EmotionStateStore.getInstance().pulseAlert(2500);

        SecretaryPersona persona = PersonaRegistry.get(context);
        String formatted = persona.format(message);

        createChannel();

        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(persona.getName())
                        .setContentText(formatted)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context)
                .notify(1001, builder.build());

        NotificationHistoryStore.get(context)
                .appendAsync(persona.getName(), persona.getName(), formatted);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "PocketSecretaryChannel",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            context.getSystemService(NotificationManager.class)
                    .createNotificationChannel(channel);
        }
    }
}
