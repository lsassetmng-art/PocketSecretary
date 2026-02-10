package com.lsam.pocketsecretary.core.template;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import java.util.*;

public class TemplateUsageStore {
    private static String key(String title){
        return "tpl_cnt_" + title;
    }
    public static void bump(Context c, String title){
        int v = Prefs.sp(c).getInt(key(title), 0);
        Prefs.sp(c).edit().putInt(key(title), v+1).apply();
    }
    public static List<EventTemplates.T> sorted(Context c){
        List<EventTemplates.T> list = new ArrayList<>(EventTemplates.list());
        list.sort((a,b)->{
            int ca = Prefs.sp(c).getInt(key(a.title), 0);
            int cb = Prefs.sp(c).getInt(key(b.title), 0);
            if (cb!=ca) return cb-ca;
            return a.title.compareTo(b.title);
        });
        return list;
    }
}
