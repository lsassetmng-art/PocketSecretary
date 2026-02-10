package com.lsam.pocketsecretary.core.voice;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class VoiceManager {
    private static VoiceEngine engine = new TtsVoiceEngine();

    public static void speak(Context c, String text){
        if (!Prefs.sp(c).getBoolean("voice_on", true)) return;
        if (engine != null && engine.isAvailable(c)){
            engine.speak(c, text);
        }
    }

    // 将来：外部サービス差し替え用
    public static void setEngine(VoiceEngine e){
        engine = e;
    }
}
