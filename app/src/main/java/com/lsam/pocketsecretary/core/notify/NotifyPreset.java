package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class NotifyPreset {
    public static void apply(Context c, String preset){
        boolean n30=false, n10=false, n5=false;
        switch (preset){
            case "morning": n30=true; n10=true; break; // JP_COMMENT
            case "noon":    n10=true; n5=true;  break; // JP_COMMENT
            case "night":   n30=true; n10=true; n5=true; break;
        }
        Prefs.putBool(c,"n30",n30);
        Prefs.putBool(c,"n10",n10);
        Prefs.putBool(c,"n5", n5);
    }
}
