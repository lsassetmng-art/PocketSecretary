package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.notification.NotificationReceiver;

public final class AutoNotifyScheduler {

    private AutoNotifyScheduler(){}

    // JP_COMMENT
    // JP_COMMENT
    public static void scheduleDemo(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long now = System.currentTimeMillis();
        // JP_COMMENT
        long at = now + 10L * 60L * 1000L;

        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, "予定：確認");
        i.putExtra(NotificationReceiver.EXTRA_TEXT, "通知が動作しました（デモ）");

        PendingIntent pi = PendingIntent.getBroadcast(
                c, 1001, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, at, pi);
    }
}
