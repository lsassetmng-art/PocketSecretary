package com.lsam.pocketsecretary.core.time;

import java.util.*;

public class FreeTimeUtil {
    // events: List<long[]{start,end}> (end荳肴・縺ｯ start+1h 莉ｮ)
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
        if (gaps.isEmpty()) return "莉頑律縺ｯ遨ｺ縺肴凾髢薙′縺ゅｊ縺ｾ縺帙ｓ";
        return "莉頑律縺ｮ遨ｺ縺肴凾髢・ "+String.join(", ", gaps);
    }
    private static String fmt(long t){
        Calendar c=Calendar.getInstance(); c.setTimeInMillis(t);
        return String.format(Locale.getDefault(),"%02d:%02d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }
}
