package com.lsam.pocketsecretary.core.metrics;

import android.util.Log;

public class DebugLogUtil {

    private static final String TAG = "PocketSecretary";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}