package com.lsam.pocketsecretary.core.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public final class NotificationChannels {

    public static final String CH_MORNING = "ps_morning";

    private NotificationChannels(){}

    public static void ensure(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        NotificationChannel ch = new NotificationChannel(
                CH_MORNING,
                "Morning",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        nm.createNotificationChannel(ch);
    }
}