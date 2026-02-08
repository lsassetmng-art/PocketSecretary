package com.lsam.pocketsecretary.core.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtil {
    public static final String CHANNEL_ID = "pocketsecretary_default";

    public static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm != null && nm.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel ch = new NotificationChannel(
                        CHANNEL_ID,
                        "PocketSecretary",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                nm.createNotificationChannel(ch);
            }
        }
    }
}
