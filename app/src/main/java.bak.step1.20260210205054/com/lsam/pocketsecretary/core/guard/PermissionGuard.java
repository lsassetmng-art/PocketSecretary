package com.lsam.pocketsecretary.core.guard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class PermissionGuard {
    public static boolean hasNotify(Context c){
        return NotificationManagerCompat.from(c).areNotificationsEnabled();
    }
    public static boolean hasCalendar(Context c){
        return ContextCompat.checkSelfPermission(
            c, Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED;
    }
}
