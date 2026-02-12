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
            new T("莨夊ｭｰ",10),
            new T("謇灘粋縺・,14),
            new T("騾夐劼",9),
            new T("繧ｸ繝",18),
            new T("蜍牙ｼｷ",20)
        );
    }
}
