package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.lsam.pocketsecretary.core.prefs.Prefs;

public class AutoNotifyScheduler {

    private static final int[] OFFSETS_MIN = new int[]{30, 10, 5};

    public static void rescheduleNext(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long now = System.currentTimeMillis();
        long until = now + 24L * 60L * 60L * 1000L;

        Cursor cursor = c.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART },
                CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " <= ?",
                new String[]{ String.valueOf(now), String.valueOf(until) },
                CalendarContract.Events.DTSTART + " ASC"
        );

        if (cursor == null) return;
        try {
            if (!cursor.moveToFirst()) return;

            String title = cursor.getString(0);
            long startAt = cursor.getLong(1);

            AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

            for (int m : OFFSETS_MIN) {
                long triggerAt = startAt - m * 60L * 1000L;
                if (triggerAt <= now) continue;

                Intent i = new Intent(c, NotificationReceiver.class);
                i.setAction("AUTO_NOTIFY");
                i.putExtra("title", "予定のリマインド");
                i.putExtra("text", m + "分後に予定があります: " + (title == null ? "(no title)" : title));

                int req = (int)(triggerAt % Integer.MAX_VALUE);
                PendingIntent pi = PendingIntent.getBroadcast(
                        c, req, i,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi);
            }
        } finally {
            cursor.close();
        }
    }
}
