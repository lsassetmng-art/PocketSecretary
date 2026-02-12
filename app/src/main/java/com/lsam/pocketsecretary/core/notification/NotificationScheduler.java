package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.lsam.pocketsecretary.R;

/**
 * Phase E: Notification 髯橸ｽｳ雋・ｽｷ髯後・+ Scheduler API
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

    // ---- Scheduler API郢晢ｽｻ郢晢ｽｻhase E 鬨ｾ蛹・ｽｽ・ｨ郢晢ｽｻ郢晢ｽｻ---
    public static void scheduleExact(Context context, long atMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (am == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, atMillis, pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, atMillis, pi);
        }
    }
}
