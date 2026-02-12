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

    // 髯昴・繝ｻ隰ｫ繧・・陞｢・ｼ繝ｻ・､陞滓・螟ゑｽｹ・ｧ繝ｻ・ｵ驛｢譎｢・ｽ・ｼ驛｢譎∽ｾｭ邵ｺ蟷・ｷｦ繝ｻ・ｮ驍ｵ・ｺ驍・ｽｲ陝蟶ｷ・ｸ・ｺ髢ｧ・ｲ騾｡繝ｻ
    public static void setEngine(VoiceEngine e){
        engine = e;
    }
}
