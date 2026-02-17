package com.lsam.pocketsecretary.core.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.receiver.EventAlarmReceiver;

public final class EventScheduler {

    private EventScheduler(){}

    public static void scheduleExact(
            Context context,
            String eventId,
            long triggerAtMillis
    ){
        Intent intent = new Intent(context, EventAlarmReceiver.class);
        intent.putExtra("event_id", eventId);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                eventId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (am != null) {
            am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pi
            );
        }
    }

    public static void cancel(
            Context context,
            String eventId
    ){
        Intent intent = new Intent(context, EventAlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                eventId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (am != null) {
            am.cancel(pi);
        }
    }
}