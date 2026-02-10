package com.lsam.pocketsecretary.infra.prefs;

import android.content.Context;

public final class AndroidPrefsStore implements PrefsStore {

    public AndroidPrefsStore(Context ctx) {}

    @Override public boolean isOnboarded() { return false; }
    @Override public void setOnboarded(boolean v) {}

    @Override public String getDefaultSecretary() { return "hiyori"; }
    @Override public void setDefaultSecretary(String id) {}

    @Override public boolean isVoiceOn() { return false; }

    @Override public int getNotifyCount() { return 0; }
    @Override public void incrementNotifyCount() {}
    @Override public void clearNotifyCount() {}
}