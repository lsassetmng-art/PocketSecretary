package com.lsam.pocketsecretary.ui.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.lsam.pocketsecretary.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT  = "text";

    private static final String CH_ID = "ps_event";

    @Override
    public void onReceive(Context context, Intent intent) {
        ensureChannel(context);

        String title = intent.getStringExtra(EXTRA_TITLE);
        String text  = intent.getStringExtra(EXTRA_TEXT);

        if (title == null) title = context.getString(R.string.notif_title_prefix) + context.getString(R.string.label_next_event);
        if (text  == null) text  = context.getString(R.string.notif_text_prefix);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CH_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int)(System.currentTimeMillis() & 0x7fffffff), b.build());
    }

    private static void ensureChannel(Context c) {
        if (Build.VERSION.SDK_INT < 26) return;
        NotificationManager nm = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel ch = new NotificationChannel(
                CH_ID,
                c.getString(R.string.notif_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        ch.setDescription(c.getString(R.string.notif_channel_desc));
        nm.createNotificationChannel(ch);
    }
}
