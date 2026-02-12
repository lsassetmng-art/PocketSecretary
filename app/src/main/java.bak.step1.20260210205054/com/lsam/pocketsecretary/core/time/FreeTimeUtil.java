package com.lsam.pocketsecretary.core.time;

import java.util.*;

public class FreeTimeUtil {
    // events: List<long[]{start,end}> (end髣包ｽｳ髢ｧ・ｴ郢晢ｽｻ驍ｵ・ｺ繝ｻ・ｯ start+1h 髣比ｼ夲ｽｽ・ｮ)
    public static String todaySummary(List<long[]> events){
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,9); c.set(Calendar.MINUTE,0);
        long workStart=c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY,18); c.set(Calendar.MINUTE,0);
        long workEnd=c.getTimeInMillis();

        events.sort(Comparator.comparingLong(a->a[0]));
        long cur=workStart;
        List<String> gaps=new ArrayList<>();

        for (long[] e: events){
            long s=e[0], en=e[1];
            if (s>cur) gaps.add(fmt(cur)+"-"+fmt(s));
            cur=Math.max(cur,en);
        }
        if (cur<workEnd) gaps.add(fmt(cur)+"-"+fmt(workEnd));
        if (gaps.isEmpty()) return "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｯ鬩包ｽｨ繝ｻ・ｺ驍ｵ・ｺ髢ｧ・ｴ陷・ｽｾ鬯ｮ・｢髦ｮ蜷ｮﾂ・ｲ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ繝ｻ;
        return "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｮ鬩包ｽｨ繝ｻ・ｺ驍ｵ・ｺ髢ｧ・ｴ陷・ｽｾ鬯ｮ・｢郢晢ｽｻ "+String.join(", ", gaps);
    }
    private static String fmt(long t){
        Calendar c=Calendar.getInstance(); c.setTimeInMillis(t);
        return String.format(Locale.getDefault(),"%02d:%02d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }
}
