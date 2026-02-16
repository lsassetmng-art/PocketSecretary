package com.lsam.pocketsecretary.core.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.lsam.pocketsecretary.core.personaos.model.PersonaToneTag;

import java.util.Locale;

public final class VoicePlayer {

    private static volatile VoicePlayer instance;

    private TextToSpeech tts;
    private volatile boolean initialized = false;

    private VoicePlayer(Context context) {

        TextToSpeech temp = new TextToSpeech(
                context.getApplicationContext(),
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        try {
                            if (VoicePlayer.this.tts != null) {
                                VoicePlayer.this.tts.setLanguage(Locale.JAPANESE);
                                initialized = true;
                            }
                        } catch (Throwable ignored) {}
                    }
                }
        );

        this.tts = temp;
    }

    public static VoicePlayer get(Context context) {
        if (instance == null) {
            synchronized (VoicePlayer.class) {
                if (instance == null) {
                    instance = new VoicePlayer(context);
                }
            }
        }
        return instance;
    }

    public void speak(String text, PersonaToneTag tone) {
        if (!initialized) return;
        if (text == null || text.isEmpty()) return;
        if (tts == null) return;

        applyVoiceProfile(tone);

        try {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PS_NOTIFY");
        } catch (Throwable ignored) {}
    }

    private void applyVoiceProfile(PersonaToneTag tone) {

        float pitch = 1.0f;
        float rate = 1.0f;

        if (tone != null) {
            switch (tone) {
                case CHEER:
                    pitch = 1.08f;
                    rate = 1.08f;
                    break;

                case ALERT:
                    pitch = 0.95f;
                    rate = 1.05f;
                    break;

                case CASUAL:
                    pitch = 1.03f;
                    rate = 1.02f;
                    break;

                case FORMAL:
                case CALM:
                default:
                    pitch = 1.0f;
                    rate = 1.0f;
                    break;
            }
        }

        try {
            if (tts != null) tts.setPitch(pitch);
        } catch (Throwable ignored) {}

        try {
            if (tts != null) tts.setSpeechRate(rate);
        } catch (Throwable ignored) {}
    }

    public void shutdown() {
        try {
            if (tts != null) tts.stop();
        } catch (Throwable ignored) {}

        try {
            if (tts != null) tts.shutdown();
        } catch (Throwable ignored) {}

        tts = null;
        initialized = false;
    }
}
