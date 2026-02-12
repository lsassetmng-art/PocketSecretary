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

    // 陝・・謫ゅ・螢ｼ・､螟慚夂ｹｧ・ｵ郢晢ｽｼ郢晁侭縺幄淦・ｮ邵ｺ邇ｲ蟠帷ｸｺ閧ｲ逡・
    public static void setEngine(VoiceEngine e){
        engine = e;
    }
}
