package com.lsam.pocketsecretary.core.secretary;

public class SecretarySpeech {

    public static String greet(Secretary s) {
        if ("ren".equals(s.id)) return "Let us check events.";
        if ("aoi".equals(s.id)) return "Preparing today events.";
        return "Good morning. Let us plan.";
    }

    public static String planLine(Secretary s, String nextEventLine) {
        if (nextEventLine == null || nextEventLine.isEmpty()) {
            if ("ren".equals(s.id)) return "No upcoming events.";
            if ("aoi".equals(s.id)) return "No upcoming events.";
            return "No upcoming events.";
        }
        if ("ren".equals(s.id)) return "次： " + nextEventLine;
        if ("aoi".equals(s.id)) return "Next event.\n" + nextEventLine;
        return "次の予定はこれだよ。\n" + nextEventLine;
    }

    public static String notifyText(Secretary s, String base) {
        if ("ren".equals(s.id)) return base;
        if ("aoi".equals(s.id)) return "お知らせ： " + base;
        return "ねえ、" + base;
    }
}
