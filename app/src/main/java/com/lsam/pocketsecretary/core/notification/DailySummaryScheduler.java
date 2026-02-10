package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.content.Intent;

public class DailySummaryScheduler {

    public static void schedule(Context c) {
        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra("title", "Daily summary");
        i.putExtra("text", "Check today’s schedule");
        NotificationScheduler.scheduleMorning(c, i);
    }
}
