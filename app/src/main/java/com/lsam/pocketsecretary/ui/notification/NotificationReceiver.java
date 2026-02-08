package com.lsam.pocketsecretary.ui.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.NotificationUtil;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT = "text";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil.ensureChannel(context);

        String title = intent.getStringExtra(EXTRA_TITLE);
        String text = intent.getStringExtra(EXTRA_TEXT);
        if (title == null) title = "PocketSecretary";
        if (text == null) text = "Notification";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtil.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify((int) (System.currentTimeMillis() & 0x7fffffff), builder.build());
    }
}
