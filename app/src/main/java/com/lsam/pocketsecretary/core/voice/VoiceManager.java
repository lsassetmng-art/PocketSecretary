package com.lsam.pocketsecretary.core.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Phase E: Voice 実動（TTS）
 */
public final class VoiceManager {

    private static TextToSpeech tts;

    private VoiceManager() {}

    public static void init(Context context) {
        if (tts != null) return;

        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.JAPAN);
            }
        });
    }

    // 既存 API
    public static void speak(String text) {
        if (tts == null) return;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ps");
    }

    // 互換用オーバーロード（ConsultActivity 等）
    public static void speak(Context context, String text) {
        init(context);
        speak(text);
    }

    public static void shutdown() {
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
    }
}