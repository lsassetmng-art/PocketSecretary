package com.lsam.pocketsecretary.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TtsEngine {

    private TextToSpeech tts;

    public TtsEngine(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.JAPAN);
            }
        });
    }

    public void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.shutdown();
        }
    }
}