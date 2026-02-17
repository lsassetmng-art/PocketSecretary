package com.lsam.pocketsecretary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.worker.EventNotificationWorker;

public class EventAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;

        String eventId = intent.getStringExtra("event_id");
        if (eventId == null) return;

        Data data = new Data.Builder()
                .putString(EventNotificationWorker.KEY_EVENT_ID, eventId)
                .build();

        OneTimeWorkRequest req =
                new OneTimeWorkRequest.Builder(EventNotificationWorker.class)
                        .setInputData(data)
                        .build();

        WorkManager.getInstance(context)
                .enqueue(req);
    }
}
