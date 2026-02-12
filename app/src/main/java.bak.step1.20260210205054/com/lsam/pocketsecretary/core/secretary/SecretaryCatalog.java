package com.lsam.pocketsecretary.core.secretary;

import java.util.ArrayList;
import java.util.List;

public class SecretaryCatalog {

    public static List<Secretary> all() {
        List<Secretary> list = new ArrayList<>();
        list.add(new Secretary("hiyori", "縺ｲ繧医ｊ", "繧・＆縺励＞"));
        list.add(new Secretary("aoi", "縺ゅ♀縺・, "髱吶°"));
        list.add(new Secretary("ren", "繧後ｓ", "遶ｯ逧・));
        return list;
    }

    public static Secretary byId(String id) {
        for (Secretary s : all()) {
            if (s.id.equals(id)) return s;
        }
        return all().get(0);
    }
}
