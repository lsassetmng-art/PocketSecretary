package com.lsam.pocketsecretary.core.secretary;

import java.util.ArrayList;
import java.util.List;

public class SecretaryCatalog {

    public static List<Secretary> all() {
        List<Secretary> list = new ArrayList<>();
        list.add(new Secretary("hiyori", "邵ｺ・ｲ郢ｧ蛹ｻ・・, "郢ｧ繝ｻ・・ｸｺ蜉ｱ・・));
        list.add(new Secretary("aoi", "邵ｺ繧・凰邵ｺ繝ｻ, "鬮ｱ蜷ｶﾂｰ"));
        list.add(new Secretary("ren", "郢ｧ蠕鯉ｽ・, "驕ｶ・ｯ騾ｧ繝ｻ));
        return list;
    }

    public static Secretary byId(String id) {
        for (Secretary s : all()) {
            if (s.id.equals(id)) return s;
        }
        return all().get(0);
    }
}
