package com.lsam.pocketsecretary.core.template;

import java.util.Arrays;
import java.util.List;

public class EventTemplates {
    public static class T {
        public final String title; public final int hour;
        public T(String t,int h){ title=t; hour=h; }
    }
    public static List<T> list(){
        return Arrays.asList(
            new T("闔ｨ螟奇ｽｭ・ｰ",10),
            new T("隰・§邊狗ｸｺ繝ｻ,14),
            new T("鬨ｾ螟仙款",9),
            new T("郢ｧ・ｸ郢晢｣ｰ",18),
            new T("陷咲甥・ｼ・ｷ",20)
        );
    }
}
