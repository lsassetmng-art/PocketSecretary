package com.lsam.pocketsecretary.ui.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TEXT  = "extra_text";

    @Override
    public void onReceive(Context c, Intent i) {
        // v2.x stub (safe)
    }
}