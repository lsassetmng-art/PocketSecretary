package com.lsam.pocketsecretary.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;
import com.lsam.pocketsecretary.service.NotificationService;

public class ReminderWorker extends Worker {

    private static final String CHANNEL_ID = "pocket_secretary_channel";

    public ReminderWorker(@NonNull Context context,
                          @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        String title = getApplicationContext().getString(R.string.notify_title);

        String message = getInputData().getString("message");
        if (TextUtils.isEmpty(message)) {
            message = getApplicationContext().getString(R.string.notify_text_default);
        }

        createNotificationChannel();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(getApplicationContext())
                .notify(1001, builder.build());

        // 履歴保存
        new NotificationService(getApplicationContext(), null)
                .notifyAndRecord("worker", message);

        return Result.success();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "PocketSecretaryChannel",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            NotificationManager manager =
                    getApplicationContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}