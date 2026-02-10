package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class NotifyCooldown {
    private static final String KEY_ID="cool_event_id";
    private static final String KEY_CNT="cool_cnt";

    public static boolean allow(Context c, String eventId){
        String id = Prefs.sp(c).getString(KEY_ID,"");
        int cnt = Prefs.sp(c).getInt(KEY_CNT,0);
        if (!eventId.equals(id)){
            Prefs.sp(c).edit().putString(KEY_ID,eventId).putInt(KEY_CNT,1).apply();
            return true;
        }
        if (cnt>=4) return false;
        Prefs.sp(c).edit().putInt(KEY_CNT,cnt+1).apply();
        return true;
    }

    public static void reset(Context c){
        Prefs.sp(c).edit().remove(KEY_ID).remove(KEY_CNT).apply();
    }
}
