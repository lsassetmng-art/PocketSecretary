package com.lsam.pocketsecretary.core.secretary;

/**
 * Secretary
 * Phase B 豁｣譛ｬ繝｢繝・Ν
 * ID 縺ｯ String・・num遖∵ｭ｢・・ */
public final class Secretary {

    private final String id;
    private final String displayName;

    public Secretary(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}