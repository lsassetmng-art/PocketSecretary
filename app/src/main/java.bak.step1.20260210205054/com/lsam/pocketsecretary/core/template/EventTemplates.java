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
            new T("髣費ｽｨ陞溷･・ｽｽ・ｭ繝ｻ・ｰ",10),
            new T("髫ｰ繝ｻﾂｧ驍顔距・ｸ・ｺ郢晢ｽｻ,14),
            new T("鬯ｨ・ｾ陞滉ｻ呎ｬｾ",9),
            new T("驛｢・ｧ繝ｻ・ｸ驛｢譎｢・｣・ｰ",18),
            new T("髯ｷ蜥ｲ逕･繝ｻ・ｼ繝ｻ・ｷ",20)
        );
    }
}
