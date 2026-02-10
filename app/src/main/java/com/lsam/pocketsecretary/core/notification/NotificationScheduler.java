package com.lsam.pocketsecretary.core.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.lsam.pocketsecretary.R;

/**
 * Phase E: Notification 実動
 */
public final class NotificationScheduler {

    private static final String CHANNEL_ID = "pocketsecretary_default";

    private NotificationScheduler() {}

    public static void notify(Context context, String title, String body) {
        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            nm.createNotificationChannel(ch);
        }

        NotificationCompat.Builder b =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        nm.notify((int) System.currentTimeMillis(), b.build());
    }
}