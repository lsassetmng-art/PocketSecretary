package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.prefs.Prefs;

public class AutoNotifyScheduler {

    private static final int[] OFFSETS_MIN = new int[]{30, 10, 5};

    public static void rescheduleNext(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        NextEventPicker.Picked ev = NextEventPicker.pick(c);
        if (ev == null) return;

        long now = System.currentTimeMillis();
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        for (int m : OFFSETS_MIN) {
            long triggerAt = ev.startAt - m * 60L * 1000L;
            if (triggerAt <= now) continue;

            Intent i = new Intent(c, NotificationReceiver.class);
            i.setAction("AUTO_NOTIFY");
            i.putExtra("title", "予定のリマインド");
            i.putExtra("text", m + "分後に予定があります: " + (ev.title == null ? "(no title)" : ev.title));

            int req = (int)(triggerAt % Integer.MAX_VALUE);
            PendingIntent pi = PendingIntent.getBroadcast(
                    c, req, i,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
        }
    }
}
