package com.lsam.pocketsecretary.core.personaos.util;

public final class TextUtil {
    private TextUtil(){}

    public static String trimTo(String s, int maxChars) {
        if (s == null) return "";
        int m = maxChars;
        if (m < 20) m = 20;
        if (m > 240) m = 240;
        if (s.length() <= m) return s;
        return s.substring(0, m);
    }
}