package com.lsam.pocketsecretary.infra.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * AndroidPrefsStore
 * Phase A1 雎・ｽ｣隴幢ｽｬ陞ｳ貅ｯ・｣繝ｻ */
public final class AndroidPrefsStore implements PrefsStore {

    private static final String NAME = "pocketsecretary_prefs";

    private static final String K_ONBOARDED = "onboarded";
    private static final String K_DEFAULT_SECRETARY = "default_secretary";
    private static final String K_VOICE_ENABLED = "voice_enabled";
    private static final String K_NOTIFY_COUNT = "notify_count";

    private final SharedPreferences sp;

    public AndroidPrefsStore(Context ctx) {
        this.sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isOnboarded() {
        return sp.getBoolean(K_ONBOARDED, false);
    }

    @Override
    public void setOnboarded(boolean v) {
        sp.edit().putBoolean(K_ONBOARDED, v).apply();
    }

    @Override
    public String getDefaultSecretary() {
        return sp.getString(K_DEFAULT_SECRETARY, "hiyori");
    }

    @Override
    public void setDefaultSecretary(String id) {
        sp.edit().putString(K_DEFAULT_SECRETARY, id).apply();
    }

    @Override
    public boolean isVoiceEnabled() {
        return sp.getBoolean(K_VOICE_ENABLED, false);
    }

    @Override
    public void setVoiceEnabled(boolean v) {
        sp.edit().putBoolean(K_VOICE_ENABLED, v).apply();
    }

    @Override
    public int getNotifyCount() {
        return sp.getInt(K_NOTIFY_COUNT, 0);
    }

    @Override
    public void incrementNotifyCount() {
        sp.edit().putInt(K_NOTIFY_COUNT, getNotifyCount() + 1).apply();
    }

    @Override
    public void clearNotifyCount() {
        sp.edit().putInt(K_NOTIFY_COUNT, 0).apply();
    }
}
