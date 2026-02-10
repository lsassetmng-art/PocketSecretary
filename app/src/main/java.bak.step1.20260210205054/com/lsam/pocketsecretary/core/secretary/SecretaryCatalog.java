package com.lsam.pocketsecretary.core.secretary;

import java.util.ArrayList;
import java.util.List;

public class SecretaryCatalog {

    public static List<Secretary> all() {
        List<Secretary> list = new ArrayList<>();
        list.add(new Secretary("hiyori", "ひより", "やさしい"));
        list.add(new Secretary("aoi", "あおい", "静か"));
        list.add(new Secretary("ren", "れん", "端的"));
        return list;
    }

    public static Secretary byId(String id) {
        for (Secretary s : all()) {
            if (s.id.equals(id)) return s;
        }
        return all().get(0);
    }
}
