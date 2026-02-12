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
            minutes + "蛻・ｾ後↓莠亥ｮ壹〒縺・ " + title,
            "縺ｾ繧ゅ↑縺丈ｺ亥ｮ壹〒縺呻ｼ・ + minutes + "蛻・ｼ・ " + title,
            "莠亥ｮ壹・譎る俣縺瑚ｿ代＞縺ｧ縺呻ｼ・ + minutes + "蛻・ｼ・ " + title
        };
        return pick(m);
    }

    public static String secretaryPrefix(){
        int b = band();
        if (b==0) return pick(new String[]{"縺翫・繧医≧縺斐＊縺・∪縺吶・, "莉頑律繧ら｢ｺ隱阪＠縺ｾ縺吶・});
        if (b==1) return pick(new String[]{"遒ｺ隱阪＠縺ｾ縺吶・, "谺｡縺ｮ莠亥ｮ壹〒縺吶・});
        return pick(new String[]{"縺顔夢繧後＆縺ｾ縺ｧ縺吶・, "蠢ｵ縺ｮ縺溘ａ縺顔衍繧峨○縺励∪縺吶・});
    }

    public static String contextHint(boolean tight, boolean consecutive){
        if (tight && consecutive) return "遘ｻ蜍墓凾髢薙↓豕ｨ諢上＠縺ｦ縺上□縺輔＞縲らｶ壹￠縺ｦ莠亥ｮ壹′縺ゅｊ縺ｾ縺吶・;
        if (tight) return "遘ｻ蜍墓凾髢薙↓豕ｨ諢上＠縺ｦ縺上□縺輔＞縲・;
        if (consecutive) return "縺薙・蠕後∫ｶ壹￠縺ｦ莠亥ｮ壹′縺ゅｊ縺ｾ縺吶・;
        return "";
    }

    public static String consultFollowUp(){
        return "蠢・ｦ√〒縺励◆繧峨√∪縺溯◇縺・※縺上□縺輔＞縲・;
    }
}
