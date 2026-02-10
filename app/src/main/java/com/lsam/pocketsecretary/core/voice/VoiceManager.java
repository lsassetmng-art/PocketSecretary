package com.lsam.pocketsecretary.core.voice;

import android.content.Context;

public class VoiceManager {
    private static VoiceEngine engine = new TtsVoiceEngine();

    public static void speak(Context c, String text){
        if (engine != null && engine.isAvailable(c)){
            engine.speak(c, text);
        }
    }

    // JP_COMMENT
    public static void setEngine(VoiceEngine e){
        engine = e;
    }
}
