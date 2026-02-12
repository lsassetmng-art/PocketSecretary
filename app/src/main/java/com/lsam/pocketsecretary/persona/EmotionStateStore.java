package com.lsam.pocketsecretary.persona;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class EmotionStateStore {

    public enum Emotion {
        CALM,
        ALERT,
        SPEAKING
    }

    private static final EmotionStateStore INSTANCE = new EmotionStateStore();
    public static EmotionStateStore get() { return INSTANCE; }

    private final MutableLiveData<Emotion> emotionLive =
            new MutableLiveData<>(Emotion.CALM);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int token = 0;

    private EmotionStateStore() {}

    public LiveData<Emotion> live() {
        return emotionLive;
    }

    public Emotion current() {
        Emotion e = emotionLive.getValue();
        return e == null ? Emotion.CALM : e;
    }

    public void set(Emotion e) {
        token++;
        emotionLive.postValue(e);
    }

    public void setTemporary(Emotion e, long durationMs) {
        token++;
        int myToken = token;
        emotionLive.postValue(e);

        handler.postDelayed(() -> {
            if (token != myToken) return;
            token++;
            emotionLive.postValue(Emotion.CALM);
        }, durationMs);
    }

    public void pulseAlert(long ms) {
        setTemporary(Emotion.ALERT, ms);
    }

    public void speakingOn() {
        set(Emotion.SPEAKING);
    }

    public void speakingOff() {
        set(Emotion.CALM);
    }
}
