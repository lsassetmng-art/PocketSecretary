package com.lsam.pocketsecretary.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.service.NotificationService;

public class ReminderWorker extends Worker {

    public ReminderWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        String message = getInputData().getString("message");

        if (message == null) {
            message = getApplicationContext()
                    .getString(R.string.reminder_default_message);
        }

        new NotificationService(getApplicationContext())
                .notifyAndRecord("worker", message);

        return Result.success();
    }
}
