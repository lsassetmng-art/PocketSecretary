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
            minutes + "陋ｻ繝ｻ・ｾ蠕娯・闔莠･・ｮ螢ｹ縲堤ｸｺ繝ｻ " + title,
            "邵ｺ・ｾ郢ｧ繧・・邵ｺ荳茨ｽｺ莠･・ｮ螢ｹ縲堤ｸｺ蜻ｻ・ｼ繝ｻ + minutes + "陋ｻ繝ｻ・ｼ繝ｻ " + title,
            "闔莠･・ｮ螢ｹ繝ｻ隴弱ｋ菫｣邵ｺ迹夲ｽｿ莉｣・樒ｸｺ・ｧ邵ｺ蜻ｻ・ｼ繝ｻ + minutes + "陋ｻ繝ｻ・ｼ繝ｻ " + title
        };
        return pick(m);
    }

    public static String secretaryPrefix(){
        int b = band();
        if (b==0) return pick(new String[]{"邵ｺ鄙ｫ繝ｻ郢ｧ蛹ｻ竕ｧ邵ｺ譁撰ｼ顔ｸｺ繝ｻ竏ｪ邵ｺ蜷ｶﾂ繝ｻ, "闔蛾大ｾ狗ｹｧ繧会ｽ｢・ｺ髫ｱ髦ｪ・邵ｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ});
        if (b==1) return pick(new String[]{"驕抵ｽｺ髫ｱ髦ｪ・邵ｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ, "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ縲堤ｸｺ蜷ｶﾂ繝ｻ});
        return pick(new String[]{"邵ｺ鬘泌､｢郢ｧ蠕鯉ｼ・ｸｺ・ｾ邵ｺ・ｧ邵ｺ蜷ｶﾂ繝ｻ, "陟｢・ｵ邵ｺ・ｮ邵ｺ貅假ｽ∫ｸｺ鬘碑｡咲ｹｧ蟲ｨ笳狗ｸｺ蜉ｱ竏ｪ邵ｺ蜷ｶﾂ繝ｻ});
    }

    public static String contextHint(boolean tight, boolean consecutive){
        if (tight && consecutive) return "驕假ｽｻ陷榊｢灘・鬮｢阮吮・雎包ｽｨ隲｢荳奇ｼ邵ｺ・ｦ邵ｺ荳岩味邵ｺ霈費ｼ樒ｸｲ繧会ｽｶ螢ｹ・邵ｺ・ｦ闔莠･・ｮ螢ｹ窶ｲ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ;
        if (tight) return "驕假ｽｻ陷榊｢灘・鬮｢阮吮・雎包ｽｨ隲｢荳奇ｼ邵ｺ・ｦ邵ｺ荳岩味邵ｺ霈費ｼ樒ｸｲ繝ｻ;
        if (consecutive) return "邵ｺ阮吶・陟募ｾ個竏ｫ・ｶ螢ｹ・邵ｺ・ｦ闔莠･・ｮ螢ｹ窶ｲ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ;
        return "";
    }

    public static String consultFollowUp(){
        return "陟｢繝ｻ・ｦ竏壹堤ｸｺ蜉ｱ笳・ｹｧ蟲ｨﾂ竏壺穐邵ｺ貅ｯ笳・ｸｺ繝ｻ窶ｻ邵ｺ荳岩味邵ｺ霈費ｼ樒ｸｲ繝ｻ;
    }
}
