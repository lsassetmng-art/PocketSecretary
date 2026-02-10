package com.lsam.pocketsecretary.infra.prefs;

public interface PrefsStore {
    boolean isOnboarded();
    void setOnboarded(boolean v);

    String getDefaultSecretary();
    void setDefaultSecretary(String id);

    boolean isVoiceOn();

    int getNotifyCount();
    void incrementNotifyCount();
    void clearNotifyCount();
}