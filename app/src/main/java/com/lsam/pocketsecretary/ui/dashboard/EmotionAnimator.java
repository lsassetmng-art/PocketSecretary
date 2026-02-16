package com.lsam.pocketsecretary.ui.dashboard;

import android.animation.ObjectAnimator;
import android.view.View;

import com.lsam.pocketsecretary.core.personaos.model.PersonaToneTag;

public final class EmotionAnimator {

    private EmotionAnimator(){}

    public static void apply(View view, PersonaToneTag tone) {
        if (view == null) return;

        if (tone == null) tone = PersonaToneTag.CALM;

        switch (tone) {
            case CHEER:
                ObjectAnimator.ofFloat(view, "translationY", 0f, -10f, 0f)
                        .setDuration(600).start();
                break;

            case ALERT:
                ObjectAnimator.ofFloat(view, "translationX", 0f, -8f, 8f, 0f)
                        .setDuration(400).start();
                break;

            case CASUAL:
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.02f, 1f)
                        .setDuration(1500).start();
                break;

            case FORMAL:
            case CALM:
            default:
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.015f, 1f)
                        .setDuration(1200).start();
                break;
        }
    }
}
