package com.lsam.pocketsecretary.core.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.data.todo.TodoRepository;

public class EventNotificationWorker extends Worker {

    public EventNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        long eventId = getInputData().getLong("eventId", -1L);

        // Keep existing event notification content stable; append only if needed.
        final String[] message = new String[] { "Event reminder" };

        if (eventId > 0) {
            TodoRepository repo = new TodoRepository(getApplicationContext());
            repo.countOpenByEventId(eventId, new TodoRepository.Callback<Integer>() {
                @Override public void onSuccess(Integer value) {
                    if (value != null && value > 0) {
                        message[0] = message[0] + "\nCheck related todo items.";
                    }
                    NotificationHelper.send(getApplicationContext(), message[0]);
                }

                @Override public void onError(Exception e) {
                    NotificationHelper.send(getApplicationContext(), message[0]);
                }
            });

            // Worker must finish; notification will still be sent (async).
            return Result.success();
        }

        NotificationHelper.send(getApplicationContext(), message[0]);
        return Result.success();
    }
}
