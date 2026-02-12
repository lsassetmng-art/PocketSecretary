package com.lsam.pocketsecretary.infra.prefs;

/**
 * PrefsStore
 * Phase A1 雎・ｽ｣陟台ｸ翫≧郢晢ｽｳ郢ｧ・ｿ郢晢ｽｼ郢晁ｼ斐♂郢晢ｽｼ郢ｧ・ｹ
 * UI / Domain 邵ｺ・ｯ陟｢繝ｻ笘・ｸｺ阮呻ｽ碁お讙守ｽｰ
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
