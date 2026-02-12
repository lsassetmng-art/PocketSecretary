package com.lsam.pocketsecretary.infra.prefs;

/**
 * PrefsStore
 * Phase A1 髮弱・・ｽ・｣髯溷床・ｸ鄙ｫ竕ｧ驛｢譎｢・ｽ・ｳ驛｢・ｧ繝ｻ・ｿ驛｢譎｢・ｽ・ｼ驛｢譎・ｽｼ譁絶凾驛｢譎｢・ｽ・ｼ驛｢・ｧ繝ｻ・ｹ
 * UI / Domain 驍ｵ・ｺ繝ｻ・ｯ髯滂ｽ｢郢晢ｽｻ隨倥・・ｸ・ｺ髦ｮ蜻ｻ・ｽ遒√♀隶吝ｮ茨ｽｽ・ｰ
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
