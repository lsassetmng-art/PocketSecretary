package com.lsam.pocketsecretary;

import android.app.Application;

import com.lsam.pocketsecretary.core.SupabaseManager;

public class PocketSecretaryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 🔐 起動時に匿名ログイン
        SupabaseManager.signInAnonymously();
    }
}
