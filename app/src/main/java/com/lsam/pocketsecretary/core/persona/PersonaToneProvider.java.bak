package com.lsam.pocketsecretary.core.persona;

import android.content.Context;
import com.lsam.pocketsecretary.core.emotion.EmotionEngine;

public class PersonaToneProvider {

    public static String buildMorningMessage(
            Context context,
            String persona,
            EmotionEngine.Emotion emotion,
            int todayCount
    ) {

        if (todayCount == 0) {
            return "今日は静かな一日になりそうです。";
        }

        switch (persona.toLowerCase()) {

            case "kayama":
                return kayama(emotion, todayCount);

            case "sakamoto":
                return sakamoto(emotion, todayCount);

            case "michelle":
                return michelle(emotion, todayCount);

            default:
                return "今日は予定が " + todayCount + " 件あります。";
        }
    }

    private static String kayama(
            EmotionEngine.Emotion emotion,
            int count
    ) {
        switch (emotion) {
            case ALERT:
                return "今日は予定が多めですね。余裕を持ちましょう。";
            case SPEAKING:
                return "今日は予定が " + count + " 件あります。";
            default:
                return "今日も穏やかにいきましょう。";
        }
    }

    private static String sakamoto(
            EmotionEngine.Emotion emotion,
            int count
    ) {
        if (emotion == EmotionEngine.Emotion.ALERT) {
            return "予定が詰まっています。優先順位を整理しましょう。";
        }
        return "本日の予定は " + count + " 件です。";
    }

    private static String michelle(
            EmotionEngine.Emotion emotion,
            int count
    ) {
        if (emotion == EmotionEngine.Emotion.ALERT) {
            return "今日は濃密な一日ですね。深呼吸を。";
        }
        return "今日は " + count + " の約束があります。";
    }
}
