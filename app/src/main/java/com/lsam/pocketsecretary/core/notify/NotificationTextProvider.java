package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import java.util.Random;
import java.util.Calendar;

public class NotificationTextProvider {
    private static final Random R = new Random();

    private static String pick(String[] a){
        return a[R.nextInt(a.length)];
    }

    private static int band(){
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h < 12) return 0;      // morning
        if (h < 18) return 1;      // day
        return 2;                  // night
    }

    public static String reminderLine(int minutes, String title){
        String[] m = new String[]{
            minutes + "分後に予定です: " + title,
            "まもなく予定です（" + minutes + "分）: " + title,
            "予定の時間が近いです（" + minutes + "分）: " + title
        };
        return pick(m);
    }

    public static String secretaryPrefix(){
        int b = band();
        if (b==0) return pick(new String[]{"おはようございます。", "今日も確認します。"});
        if (b==1) return pick(new String[]{"確認します。", "次の予定です。"});
        return pick(new String[]{"お疲れさまです。", "念のためお知らせします。"});
    }

    public static String contextHint(boolean tight, boolean consecutive){
        if (tight && consecutive) return "移動時間に注意してください。続けて予定があります。";
        if (tight) return "移動時間に注意してください。";
        if (consecutive) return "この後、続けて予定があります。";
        return "";
    }

    public static String consultFollowUp(){
        return "必要でしたら、また聞いてください。";
    }
}
