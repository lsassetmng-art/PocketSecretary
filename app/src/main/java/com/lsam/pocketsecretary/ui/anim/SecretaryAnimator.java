package com.lsam.pocketsecretary.ui.anim;

import android.view.View;

public class SecretaryAnimator {
    public static void breathe(View v){
        if (v==null) return;
        v.setAlpha(0f);
        v.setTranslationY(20f);
        v.animate().alpha(1f).translationY(0f).setDuration(400).start();
    }
}
