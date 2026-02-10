package com.lsam.pocketsecretary.ui.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

<<<<<<< HEAD
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TEXT  = "extra_text";

    @Override
    public void onReceive(Context c, Intent i) {
        // v2.x stub (safe)
=======
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT  = "text";

    @Override
    public void onReceive(Context context, Intent intent) {
        // safe stub (no-op)
>>>>>>> 0f68ece (fix: termux-safe minimal secretary + notification receiver stub)
    }
}