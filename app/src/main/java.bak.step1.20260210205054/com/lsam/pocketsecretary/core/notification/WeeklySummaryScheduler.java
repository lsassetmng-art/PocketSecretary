package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.content.Intent;

public class WeeklySummaryScheduler {
    public static void schedule(Context c){
        Intent i = new Intent(c, com.lsam.pocketsecretary.ui.notification.NotificationReceiver.class);
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TITLE_RES, "ps_notify_title_default");
        i.putExtra(com.lsam.pocketsecretary.ui.notification.NotificationReceiver.EXTRA_TEXT_RES,  "ps_notify_text_demo");

        long at = System.currentTimeMillis() + 180_000; // 3min demo
        NotificationScheduler.scheduleExact(c, at, i);
    }
}
