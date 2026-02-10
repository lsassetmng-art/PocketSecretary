package com.lsam.pocketsecretary.core.secretary;

public class SecretarySpeech {

    public static String greet(Secretary s) {
        if ("ren".equals(s.id)) return "予定だけ、確認しよう。";
        if ("aoi".equals(s.id)) return "今日の予定を静かに整えます。";
        return "おはよう。今日も一緒に整えようね。";
    }

    public static String planLine(Secretary s, String nextEventLine) {
        if (nextEventLine == null || nextEventLine.isEmpty()) {
            if ("ren".equals(s.id)) return "次の予定は未登録。必要なら追加して。";
            if ("aoi".equals(s.id)) return "次の予定は見つかりませんでした。";
            return "次の予定はまだ無いみたい。必要なら入れておこう。";
        }
        if ("ren".equals(s.id)) return "次： " + nextEventLine;
        if ("aoi".equals(s.id)) return "次の予定です。\n" + nextEventLine;
        return "次の予定はこれだよ。\n" + nextEventLine;
    }

    public static String notifyText(Secretary s, String base) {
        if ("ren".equals(s.id)) return base;
        if ("aoi".equals(s.id)) return "お知らせ： " + base;
        return "ねえ、" + base;
    }
}
