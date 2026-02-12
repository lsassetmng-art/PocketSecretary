package com.lsam.pocketsecretary.infra.prefs;

/**
 * PrefsStore
 * Phase A1 豁｣蠑上う繝ｳ繧ｿ繝ｼ繝輔ぉ繝ｼ繧ｹ
 * UI / Domain 縺ｯ蠢・★縺薙ｌ邨檎罰
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