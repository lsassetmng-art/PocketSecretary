package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import com.lsam.pocketsecretary.core.assets.AssetRepository;

import org.json.JSONObject;

public class VoiceConfigLoader {

    public static class VoiceConfig {
        public String engine;
        public String locale;
        public float pitch;
        public float speechRate;
        public String style;
    }

    public static VoiceConfig load(Context context,
                                   String personaId,
                                   String voiceId) {

        try {

            String path = AssetRepository.personaVoice(personaId, voiceId)
                    + "voice_config.json";

            String jsonText = AssetRepository.loadText(context, path);
            if (jsonText == null) return null;

            JSONObject obj = new JSONObject(jsonText);

            VoiceConfig config = new VoiceConfig();
            config.engine = obj.optString("engine", "android_tts");
            config.locale = obj.optString("locale", "ja-JP");
            config.pitch = (float) obj.optDouble("pitch", 1.0);
            config.speechRate = (float) obj.optDouble("speechRate", 1.0);
            config.style = obj.optString("style", "calm");

            return config;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
