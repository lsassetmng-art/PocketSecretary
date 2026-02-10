package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.content.Intent;

public class WeeklySummaryScheduler {

    public static void schedule(Context c) {
        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra("title", "Weekly summary");
        i.putExtra("text", "Review this week’s plans");
        NotificationScheduler.scheduleWeekly(c, i);
    }
}
