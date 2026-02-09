package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.notification.NotificationReceiver;

public class NotificationScheduler {

    public static void fireNow(Context c, String title, String text) {
        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, text);
        c.sendBroadcast(i);
    }

    public static void scheduleAt(Context c, long atMillis, String title, String text) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, title);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, text);

        PendingIntent pi = PendingIntent.getBroadcast(
                c,
                1,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Prefs.saveScheduled(c, atMillis, title, text);

        if (Build.VERSION.SDK_INT >= 23) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, atMillis, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, atMillis, pi);
        }
    }

    public static void restoreIfAny(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long at = Prefs.getScheduledAt(c);
        if (at <= 0) return;

        String title = Prefs.getScheduledTitle(c);
        String text = Prefs.getScheduledText(c);
        if (title == null || text == null) return;

        long now = System.currentTimeMillis();
        if (at < now + 5_000L) {
            Prefs.clearScheduled(c);
            return;
        }
        scheduleAt(c, at, title, text);
    }
}
