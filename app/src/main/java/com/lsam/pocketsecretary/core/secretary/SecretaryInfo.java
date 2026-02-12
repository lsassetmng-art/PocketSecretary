package com.lsam.pocketsecretary.core.secretary;

public final class SecretaryInfo {

    private final String id;
    private final String displayName;

    public SecretaryInfo(String id, String displayName) {
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
