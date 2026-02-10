package com.lsam.pocketsecretary.core.secretary;

/**
 * Secretary
 * Phase B 正本モデル
 * ID は String（enum禁止）
 */
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