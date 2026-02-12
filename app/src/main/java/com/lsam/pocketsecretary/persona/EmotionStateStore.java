package com.lsam.pocketsecretary.persona;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class EmotionStateStore {

    public enum Emotion {
        CALM,
        FOCUS,
        TIGHT,
        HAPPY,
        SPEAKING,
        ALERT
    }

    private static final EmotionStateStore INSTANCE = new EmotionStateStore();

    private final MutableLiveData<Emotion> emotionLiveData =
            new MutableLiveData<>(Emotion.CALM);

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private EmotionStateStore() {}

    public static EmotionStateStore getInstance() {
        return INSTANCE;
    }

    public static EmotionStateStore get() {
        return INSTANCE;
    }

    public Emotion current() {
        Emotion e = emotionLiveData.getValue();
        return e != null ? e : Emotion.CALM;
    }

    public LiveData<Emotion> asLiveData() {
        return emotionLiveData;
    }

    public void set(Emotion emotion) {
        // ALERT 繧貞・驛ｨ逧・↓ TIGHT 縺ｫ豁｣隕丞喧
        if (emotion == Emotion.ALERT) {
            emotion = Emotion.TIGHT;
        }
        final Emotion finalEmotion = emotion;
        mainHandler.post(() -> emotionLiveData.setValue(finalEmotion));
    }

    public void pulseAlert(long durationMs) {
        set(Emotion.TIGHT);
        mainHandler.postDelayed(() -> set(Emotion.CALM), durationMs);
    }
}