package com.lsam.pocketsecretary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.worker.EventNotificationWorker;

public class EventAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String eventId = intent.getStringExtra("event_id");
        if (eventId == null) return;

        EventNotificationWorker.scheduleNext(
                context,
                eventId,
                System.currentTimeMillis()
        );
    }
}