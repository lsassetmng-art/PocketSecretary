package com.lsam.PocketSecretary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Presence-only receiver.
 * Intentionally does nothing.
 */
public class PresenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // silence by design
    }
}
