package com.lsam.pocketsecretary.ui.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.notification.AutoNotifyScheduler;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AutoNotifyScheduler.rescheduleNext(context);
        }
    }
}
