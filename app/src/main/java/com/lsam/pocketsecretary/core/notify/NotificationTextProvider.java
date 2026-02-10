// DISABLED_FOR_PLAY_SAFETY
package com.lsam.pocketsecretary.core.notify;

import java.util.*;

public class NotificationTextProvider {
    private static final Random R = new Random();

    private static String pick(String[] a){ return a[R.nextInt(a.length)]; }

    public static String prefix(){
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (h < 12) return pick(new String[]{"Good morning.","Checking today."});
        if (h < 18) return pick(new String[]{"Checking.","Next event."});
        return pick(new String[]{"Hello.","Just a reminder."});
    }

    public static String body(int min, String title){
        return pick(new String[]{
            min+"JP_TEXT: "+title,
            "JP_TEXT（"+min+"JP_TEXT）: "+title,
            "JP_TEXT（"+min+"JP_TEXT）: "+title
        });
    }

    public static String hint(boolean tight, boolean cont){
        if (tight && cont) return "JP_TEXT。JP_TEXT。";
        if (tight) return "JP_TEXT。";
        if (cont) return "JP_TEXT、JP_TEXT。";
        return "";
    }

    public static String consultFollowUp(){
        return "JP_TEXT、JP_TEXT。";
    }
}
