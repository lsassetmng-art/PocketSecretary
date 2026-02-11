package com.lsam.pocketsecretary.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.AppConstants;

public class NotificationHelper {

    public static void showNotification(Context context, String title, String text) {

        createChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context)
                .notify(AppConstants.NOTIFICATION_ID, builder.build());
    }

    private static void createChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            AppConstants.CHANNEL_ID,
                            "PocketSecretary",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}