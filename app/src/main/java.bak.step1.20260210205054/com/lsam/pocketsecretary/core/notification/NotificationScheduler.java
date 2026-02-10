package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationScheduler {

    // ---- compatibility layer ----
    public static void fireNow(Context c, String title, String text){
        Intent i = new Intent(c, com.lsam.pocketsecretary.ui.notification.NotificationReceiver.class);
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TEXT, text);
        notifyNow(c, i);
    }

    public static void scheduleAt(Context c, long atMillis, String title, String text){
        Intent i = new Intent(c, com.lsam.pocketsecretary.ui.notification.NotificationReceiver.class);
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TEXT, text);
        scheduleExact(c, atMillis, i);
    }

    // ---- new minimal core ----
    public static void notifyNow(Context c, Intent i){
        com.lsam.pocketsecretary.core.notification.MinimalNotifier.notifyNow(c, i);
    }

    public static void scheduleExact(Context c, long atMillis, Intent i){
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;

        PendingIntent pi = PendingIntent.getBroadcast(
            c, 1, i, flags
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, atMillis, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, atMillis, pi);
        }
    }
}
