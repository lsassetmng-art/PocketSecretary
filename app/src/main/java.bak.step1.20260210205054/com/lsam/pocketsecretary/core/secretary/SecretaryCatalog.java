package com.lsam.pocketsecretary.core.secretary;

import java.util.ArrayList;
import java.util.List;

public class SecretaryCatalog {

    public static List<Secretary> all() {
        List<Secretary> list = new ArrayList<>();
        list.add(new Secretary("hiyori", "驍ｵ・ｺ繝ｻ・ｲ驛｢・ｧ陋ｹ・ｻ繝ｻ繝ｻ, "驛｢・ｧ郢晢ｽｻ繝ｻ繝ｻ・ｸ・ｺ陷会ｽｱ繝ｻ繝ｻ));
        list.add(new Secretary("aoi", "驍ｵ・ｺ郢ｧ繝ｻ蜃ｰ驍ｵ・ｺ郢晢ｽｻ, "鬯ｮ・ｱ陷ｷ・ｶ・ゑｽｰ"));
        list.add(new Secretary("ren", "驛｢・ｧ陟暮ｯ会ｽｽ繝ｻ, "鬩包ｽｶ繝ｻ・ｯ鬨ｾ・ｧ郢晢ｽｻ));
        return list;
    }

    public static Secretary byId(String id) {
        for (Secretary s : all()) {
            if (s.id.equals(id)) return s;
        }
        return all().get(0);
    }
}
