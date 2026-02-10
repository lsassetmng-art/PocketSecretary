package com.lsam.pocketsecretary.core.voice;

import android.content.Context;

public interface VoiceEngine {
    boolean isAvailable(Context c);
    void speak(Context c, String text);
}
