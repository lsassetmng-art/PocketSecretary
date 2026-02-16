package com.lsam.pocketsecretary.service;

import com.lsam.pocketsecretary.core.voice.VoicePlayer;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;
import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.PersonaResponse;
import com.lsam.pocketsecretary.core.personaos.model.PersonaToneTag;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;

import java.util.Map;

public class NotificationService {

    private static final String CHANNEL_ID = "pocket_secretary_channel";
    private final Context context;

    public NotificationService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void notifyAndRecord(String source, String message) {

        String personaId = CurrentPersonaStore.get(context);

        Map<String, String> yaml = PersonaYamlLoader.load(context, personaId);

        String displayName = yaml.get("display_name");
        if (displayName == null) displayName = personaId;

        PersonaResponse response = PersonaEngine.generate(
                context,
                personaId,
                EmotionState.CALM,
                PersonaChannel.DASHBOARD,
                message,
                null,
                0
        );

        String generated = response.text;
        PersonaToneTag tone =
                response.toneTag != null
                        ? response.toneTag
                        : PersonaToneTag.CALM;

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
                        .setContentTitle(displayName)
                        .setContentText(generated)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        VoicePlayer.get(context).speak(generated, tone);

        NotificationManagerCompat.from(context)
                .notify(1001, builder.build());

        NotificationHistoryStore.get(context)
                .appendAsync(displayName, displayName, generated);
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
