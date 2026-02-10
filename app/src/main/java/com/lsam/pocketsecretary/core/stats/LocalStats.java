package com.lsam.pocketsecretary.core.stats;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class LocalStats {
    public static void incLaunch(Context c){
        Prefs.sp(c).edit().putInt("stat_launch",
            Prefs.sp(c).getInt("stat_launch",0)+1).apply();
    }
    public static void incNotify(Context c){
        Prefs.sp(c).edit().putInt("stat_notify",
            Prefs.sp(c).getInt("stat_notify",0)+1).apply();
    }
    public static String summary(Context c){
        int l=Prefs.sp(c).getInt("stat_launch",0);
        int n=Prefs.sp(c).getInt("stat_notify",0);
        return "起動 "+l+"回 / 通知 "+n+"回";
    }
}
