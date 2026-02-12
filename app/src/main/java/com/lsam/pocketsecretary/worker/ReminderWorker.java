package com.lsam.pocketsecretary.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
            message = "縺比ｺ亥ｮ壹・縺頑凾髢薙↓縺ｪ繧翫∪縺励◆";
        }

        new NotificationService(getApplicationContext())
                .notifyAndRecord("worker", message);

        return Result.success();
    }
}