package com.lsam.pocketsecretary.ui.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.notification.MinimalNotifier;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT  = "text";

    // strings.xml key (name) to resolve
    public static final String EXTRA_TITLE_RES = "title_res";
    public static final String EXTRA_TEXT_RES  = "text_res";

    @Override public void onReceive(Context c, Intent i) {
        // resolve via strings if provided
        String title = i.getStringExtra(EXTRA_TITLE);
        String text  = i.getStringExtra(EXTRA_TEXT);

        String titleRes = i.getStringExtra(EXTRA_TITLE_RES);
        String textRes  = i.getStringExtra(EXTRA_TEXT_RES);

        if (titleRes != null) {
            int id = c.getResources().getIdentifier(titleRes, "string", c.getPackageName());
            if (id != 0) title = c.getString(id);
        }
        if (textRes != null) {
            int id = c.getResources().getIdentifier(textRes, "string", c.getPackageName());
            if (id != 0) text = c.getString(id);
        }

        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_TEXT, text);

        MinimalNotifier.notifyNow(c, i);
    }
}
