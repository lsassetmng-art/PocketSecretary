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
            minutes + "髯具ｽｻ郢晢ｽｻ繝ｻ・ｾ陟募ｨｯ繝ｻ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ邵ｲ蝣､・ｸ・ｺ郢晢ｽｻ " + title,
            "驍ｵ・ｺ繝ｻ・ｾ驛｢・ｧ郢ｧ繝ｻ繝ｻ驍ｵ・ｺ闕ｳ闌ｨ・ｽ・ｺ闔・･繝ｻ・ｮ陞｢・ｹ邵ｲ蝣､・ｸ・ｺ陷ｻ・ｻ繝ｻ・ｼ郢晢ｽｻ + minutes + "髯具ｽｻ郢晢ｽｻ繝ｻ・ｼ郢晢ｽｻ " + title,
            "髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ髫ｴ蠑ｱ・玖将・｣驍ｵ・ｺ霑ｹ螟ｲ・ｽ・ｿ闔会ｽ｣繝ｻ讓抵ｽｸ・ｺ繝ｻ・ｧ驍ｵ・ｺ陷ｻ・ｻ繝ｻ・ｼ郢晢ｽｻ + minutes + "髯具ｽｻ郢晢ｽｻ繝ｻ・ｼ郢晢ｽｻ " + title
        };
        return pick(m);
    }

    public static String secretaryPrefix(){
        int b = band();
        if (b==0) return pick(new String[]{"驍ｵ・ｺ驗呻ｽｫ郢晢ｽｻ驛｢・ｧ陋ｹ・ｻ遶包ｽｧ驍ｵ・ｺ隴∵腸・ｼ鬘費ｽｸ・ｺ郢晢ｽｻ遶擾ｽｪ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ, "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｹ・ｧ郢ｧ莨夲ｽｽ・｢繝ｻ・ｺ鬮ｫ・ｱ鬮ｦ・ｪ繝ｻ・ｰ驍ｵ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ});
        if (b==1) return pick(new String[]{"鬩墓慣・ｽ・ｺ鬮ｫ・ｱ鬮ｦ・ｪ繝ｻ・ｰ驍ｵ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ, "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ邵ｲ蝣､・ｸ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ});
        return pick(new String[]{"驍ｵ・ｺ鬯俶ｳ鯉ｽ､・｢驛｢・ｧ陟暮ｯ会ｽｼ繝ｻ・ｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ繝ｻ・ｧ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ, "髯滂ｽ｢繝ｻ・ｵ驍ｵ・ｺ繝ｻ・ｮ驍ｵ・ｺ雋・∞・ｽ竏ｫ・ｸ・ｺ鬯倡｢托ｽ｡蜥ｲ・ｹ・ｧ陝ｲ・ｨ隨ｳ迢暦ｽｸ・ｺ陷会ｽｱ遶擾ｽｪ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ});
    }

    public static String contextHint(boolean tight, boolean consecutive){
        if (tight && consecutive) return "鬩募∞・ｽ・ｻ髯ｷ讎奇ｽ｢轣倥・鬯ｮ・｢髦ｮ蜷ｮ繝ｻ髮主桁・ｽ・ｨ髫ｲ・｢闕ｳ螂・ｽｼ・ｰ驍ｵ・ｺ繝ｻ・ｦ驍ｵ・ｺ闕ｳ蟯ｩ蜻ｳ驍ｵ・ｺ髴郁ｲｻ・ｼ讓抵ｽｸ・ｲ郢ｧ莨夲ｽｽ・ｶ陞｢・ｹ繝ｻ・ｰ驍ｵ・ｺ繝ｻ・ｦ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ遯ｶ・ｲ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
        if (tight) return "鬩募∞・ｽ・ｻ髯ｷ讎奇ｽ｢轣倥・鬯ｮ・｢髦ｮ蜷ｮ繝ｻ髮主桁・ｽ・ｨ髫ｲ・｢闕ｳ螂・ｽｼ・ｰ驍ｵ・ｺ繝ｻ・ｦ驍ｵ・ｺ闕ｳ蟯ｩ蜻ｳ驍ｵ・ｺ髴郁ｲｻ・ｼ讓抵ｽｸ・ｲ郢晢ｽｻ;
        if (consecutive) return "驍ｵ・ｺ髦ｮ蜷ｶ繝ｻ髯溷供・ｾ蛟仰遶擾ｽｫ繝ｻ・ｶ陞｢・ｹ繝ｻ・ｰ驍ｵ・ｺ繝ｻ・ｦ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ遯ｶ・ｲ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
        return "";
    }

    public static String consultFollowUp(){
        return "髯滂ｽ｢郢晢ｽｻ繝ｻ・ｦ遶丞｣ｹﾂ蝣､・ｸ・ｺ陷会ｽｱ隨ｳ繝ｻ・ｹ・ｧ陝ｲ・ｨ・つ遶丞｣ｺ遨宣し・ｺ雋・ｽｯ隨ｳ繝ｻ・ｸ・ｺ郢晢ｽｻ遯ｶ・ｻ驍ｵ・ｺ闕ｳ蟯ｩ蜻ｳ驍ｵ・ｺ髴郁ｲｻ・ｼ讓抵ｽｸ・ｲ郢晢ｽｻ;
    }
}
