package com.lsam.pocketsecretary.core.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class MinimalNotifier {

    private static final String CH_ID = "ps_basic";

    public static void notifyNow(Context c, Intent i){
        ensureChannel(c);

        String title = i.getStringExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TITLE);
        String text  = i.getStringExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TEXT);

        NotificationCompat.Builder b =
            new NotificationCompat.Builder(c, CH_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title == null ? "PocketSecretary" : title)
                .setContentText(text == null ? "" : text)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify((int)System.currentTimeMillis(), b.build());
    }

    private static void ensureChannel(Context c){
        if (android.os.Build.VERSION.SDK_INT < 26) return;
        NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;
        NotificationChannel ch = new NotificationChannel(CH_ID, "Basic", NotificationManager.IMPORTANCE_DEFAULT);
        nm.createNotificationChannel(ch);
    }
}
