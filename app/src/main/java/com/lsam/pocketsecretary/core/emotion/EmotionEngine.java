package com.lsam.pocketsecretary.core.emotion;

import android.content.Context;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;

public class EmotionEngine {

    public enum Emotion {
        CALM,
        SPEAKING,
        ALERT,
        FOCUS,
        EVENING
    }

    public static Emotion evaluate(Context context) {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        if (hour >= 22 || hour < 5) {
            return Emotion.EVENING;
        }

        int todayCount = SimpleEventStore.countToday(context);

        if (todayCount == 0) return Emotion.CALM;
        if (todayCount <= 2) return Emotion.SPEAKING;

        return Emotion.ALERT;
    }
}
