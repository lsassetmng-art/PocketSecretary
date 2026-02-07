package com.lsam.PocketSecretary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Presence-only service.
 * No sound, no action, no side effects.
 */
public class PresenceService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
