package com.lsam.pocketsecretary.core.secretary;

/**
 * SecretarySpeech
 * Phase B 正本
 * 文言生成のみを責務とする（I/O禁止）
 */
public final class SecretarySpeech {

    private SecretarySpeech() {}

    public static String greet(Secretary s) {
        return s.getDisplayName() + "です。よろしくお願いします。";
    }

    public static String planLine(Secretary s, String next) {
        if (next == null || next.isEmpty()) {
            return "今日は特に予定は入っていません。";
        }
        return "次の予定は「" + next + "」です。";
    }

    public static String notifyText(Secretary s, String baseText) {
        if (baseText == null) return "";
        return baseText;
    }
}