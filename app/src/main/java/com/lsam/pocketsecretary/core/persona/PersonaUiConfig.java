package com.lsam.pocketsecretary.core.persona;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.persona.EmotionStateStore;

public final class PersonaUiConfig {

    private PersonaUiConfig() {}

    private static EmotionStateStore.Emotion current() {
        return EmotionStateStore.get().current();
    }

    public static int resolveBackgroundColor(Context context) {
        switch (current()) {
            case CALM:
                return ContextCompat.getColor(context, R.color.ps_emotion_calm_bg);
            case FOCUS:
                return ContextCompat.getColor(context, R.color.ps_emotion_focus_bg);
            case TIGHT:
                return ContextCompat.getColor(context, R.color.ps_emotion_tight_bg);
            case HAPPY:
                return ContextCompat.getColor(context, R.color.ps_emotion_happy_bg);
            case SPEAKING:
                return ContextCompat.getColor(context, R.color.ps_emotion_focus_bg);
            default:
                return ContextCompat.getColor(context, R.color.ps_emotion_calm_bg);
        }
    }

    public static int resolveCommentResId() {
        switch (current()) {
            case CALM:
                return R.string.ps_comment_calm;
            case FOCUS:
                return R.string.ps_comment_focus;
            case TIGHT:
                return R.string.ps_comment_tight;
            case HAPPY:
                return R.string.ps_comment_happy;
            case SPEAKING:
                return R.string.ps_comment_focus;
            default:
                return R.string.ps_comment_calm;
        }
    }

    public static float resolveSpeechRate() {
        switch (current()) {
            case CALM:
                return 0.9f;
            case FOCUS:
                return 1.0f;
            case TIGHT:
                return 1.1f;
            case HAPPY:
                return 1.0f;
            case SPEAKING:
                return 1.0f;
            default:
                return 1.0f;
        }
    }

    public static float resolveSpeechPitch() {
        switch (current()) {
            case HAPPY:
                return 1.1f;
            default:
                return 1.0f;
        }
    }
}