package com.lsam.pocketsecretary.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.core.schedule.RRuleEngine;
import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;
import com.lsam.pocketsecretary.service.NotificationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class EventNotificationWorker extends Worker {

    public static final String KEY_EVENT_ID = "event_id";
    private static final String TAG = "EventWorker";

    public EventNotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();
        String eventId = getInputData().getString(KEY_EVENT_ID);
        if (eventId == null) return Result.success();

        EventEntity event =
                EventDatabase.get(context)
                        .eventDao()
                        .findById(eventId);

        if (event == null) return Result.success();

        long baseStart =
                event.lastOccurrenceAt != null
                        ? event.lastOccurrenceAt
                        : event.startAt;

        String message =
                event.title + " at " +
                        formatTime(baseStart, event.timeZone);

        new NotificationService(context)
                .notifyAndRecord("event_" + eventId, message);

        // If no recurrence → stop
        if (event.recurrenceRule == null ||
                event.recurrenceRule.trim().isEmpty()) {
            return Result.success();
        }

        // Compute next from last processed occurrence
        Long nextStart = RRuleEngine.computeNextOccurrenceMillis(
                baseStart,
                event.recurrenceRule,
                event.timeZone,
                event.recurrenceUntil,
                event.recurrenceCount
        );

        if (nextStart == null) return Result.success();

        // Update lastOccurrenceAt
        event.lastOccurrenceAt = nextStart;

        if (event.recurrenceCount != null && event.recurrenceCount > 0) {
            event.recurrenceCount = event.recurrenceCount - 1;
        }

        EventDatabase.get(context)
                .eventDao()
                .update(event);

        long nextNotify =
                nextStart - event.reminderBeforeMin * 60_000L;

        if (nextNotify < System.currentTimeMillis()) {
            nextNotify = System.currentTimeMillis() + 1000L;
        }

        scheduleNext(context, eventId, nextNotify);

        return Result.success();
    }

    public static void scheduleNext(
            Context context,
            String eventId,
            long nextTimeMillis
    ) {

        long delay = nextTimeMillis - System.currentTimeMillis();
        if (delay < 0) delay = 0;

        Data data = new Data.Builder()
                .putString(KEY_EVENT_ID, eventId)
                .build();

        OneTimeWorkRequest req =
                new OneTimeWorkRequest.Builder(EventNotificationWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();

        WorkManager.getInstance(context)
                .enqueueUniqueWork(
                        "event_" + eventId,
                        ExistingWorkPolicy.REPLACE,
                        req
                );
    }

    private String formatTime(long millis, String tz) {
        TimeZone zone =
                TimeZone.getTimeZone(
                        tz != null ? tz : "Asia/Tokyo"
                );
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        sdf.setTimeZone(zone);
        return sdf.format(new Date(millis));
    }
}
