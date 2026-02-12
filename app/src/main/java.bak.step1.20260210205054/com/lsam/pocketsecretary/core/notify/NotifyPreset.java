package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class NotifyPreset {
    public static void apply(Context c, String preset){
        boolean n30=false, n10=false, n5=false;
        switch (preset){
            case "morning": n30=true; n10=true; break; // 髫ｴ蟶ｶ莠｢郢晢ｽｻ髫ｴ魃会ｽｽ・ｩ驛｢・ｧ郢晢ｽｻ
            case "noon":    n10=true; n5=true;  break; // 髫ｴ謫ｾ・ｽ・ｼ驍ｵ・ｺ繝ｻ・ｯ鬨ｾ・ｶ繝ｻ・ｴ髯ｷ莉｣繝ｻ
            case "night":   n30=true; n10=true; n5=true; break;
        }
        Prefs.putBool(c,"n30",n30);
        Prefs.putBool(c,"n10",n10);
        Prefs.putBool(c,"n5", n5);
    }
}
