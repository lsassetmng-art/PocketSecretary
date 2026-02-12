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

    // 蟆・擂・壼､夜Κ繧ｵ繝ｼ繝薙せ蟾ｮ縺玲崛縺育畑
    public static void setEngine(VoiceEngine e){
        engine = e;
    }
}
