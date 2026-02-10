package com.lsam.pocketsecretary.infra.prefs;

/**
 * PrefsStore
 * Phase A1 正式インターフェース
 * UI / Domain は必ずこれ経由
 */
public interface PrefsStore {

    // onboarding
    boolean isOnboarded();
    void setOnboarded(boolean v);

    // secretary
    String getDefaultSecretary();
    void setDefaultSecretary(String id);

    // voice
    boolean isVoiceEnabled();
    void setVoiceEnabled(boolean v);

    // notification
    int getNotifyCount();
    void incrementNotifyCount();
    void clearNotifyCount();
}