package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.content.Intent;

public class FollowUpScheduler {

    public static void schedule(Context c) {
        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra("title", "Reminder");
        i.putExtra("text", "You have an upcoming event");
        NotificationScheduler.scheduleSoon(c, i);
    }
}
