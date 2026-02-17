package com.lsam.pocketsecretary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;
import com.lsam.pocketsecretary.core.schedule.EventScheduler;

import java.util.List;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;
        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) return;

        new Thread(() -> {

            List<EventEntity> list =
                    EventDatabase.get(context)
                            .eventDao()
                            .listAll();

            for (EventEntity e : list) {

                long notifyAt =
                        e.startAt - e.reminderBeforeMin * 60_000L;

                if (notifyAt > System.currentTimeMillis()) {
                    EventScheduler.scheduleExact(
                            context,
                            e.id,
                            notifyAt
                    );
                }
            }

        }).start();
    }
}