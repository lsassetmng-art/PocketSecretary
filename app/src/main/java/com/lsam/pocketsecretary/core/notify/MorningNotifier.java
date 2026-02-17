package com.lsam.pocketsecretary.core.notify;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.lsam.pocketsecretary.R;

public final class MorningNotifier {

    private MorningNotifier(){}

    public static void show(Context c, String title, String text) {

        NotificationChannels.ensure(c);

        NotificationCompat.Builder b =
                new NotificationCompat.Builder(c, NotificationChannels.CH_MORNING)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager nm =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        if (nm != null) {
            nm.notify(1001, b.build());
        }
    }
}