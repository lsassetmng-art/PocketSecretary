package com.lsam.pocketsecretary.core.secretary;

import android.content.Context;
import com.lsam.pocketsecretary.R;

public final class SecretarySpeech {

    private SecretarySpeech() {}

    public static String greet(Context context, Secretary s) {
        return s.getDisplayName() + context.getString(R.string.speech_greet_suffix);
    }

    public static String planLine(Context context, Secretary s, String next) {

        if (next == null || next.isEmpty()) {
            return context.getString(R.string.speech_no_plan);
        }

        return context.getString(R.string.speech_next_plan_prefix)
                + next
                + context.getString(R.string.speech_next_plan_suffix);
    }

    public static String notifyText(Secretary s, String baseText) {
        if (baseText == null) return "";
        return baseText;
    }
}
