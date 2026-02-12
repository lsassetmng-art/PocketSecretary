package com.lsam.pocketsecretary.core.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TtsVoiceEngine implements VoiceEngine {
    private TextToSpeech tts;

    @Override public boolean isAvailable(Context c){
        return true; // 鬩包ｽｶ繝ｻ・ｯ髫ｴ蟷｢・ｽ・ｫTTS髯ｷ鬘後＊驗ゑｽｲ
    }

    @Override public void speak(Context c, String text){
        if (tts == null){
            tts = new TextToSpeech(c.getApplicationContext(), status -> {
                if (status == TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.JAPAN);
                    tts.setSpeechRate(0.9f);
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ps_tts");
                }
            });
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ps_tts");
        }
    }
}
