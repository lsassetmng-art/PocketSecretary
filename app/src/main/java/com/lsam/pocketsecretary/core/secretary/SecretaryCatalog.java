package com.lsam.pocketsecretary.core.secretary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SecretaryCatalog
 * Phase B 正本
 * 秘書定義の唯一の出入口
 */
public final class SecretaryCatalog {

    private static final List<Secretary> ALL;

    static {
        List<Secretary> list = new ArrayList<>();
        list.add(new Secretary("hiyori", "ひより"));
        list.add(new Secretary("aoi", "あおい"));
        list.add(new Secretary("ren", "れん"));
        ALL = Collections.unmodifiableList(list);
    }

    private SecretaryCatalog() {}

    public static List<Secretary> list() {
        return ALL;
    }

    public static Secretary findById(String id) {
        for (Secretary s : ALL) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return ALL.get(0); // default fallback
    }
}