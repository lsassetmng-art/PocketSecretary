package com.lsam.pocketsecretary.core.secretary;

/**
 * Secretary
 * Phase B 雎・ｽ｣隴幢ｽｬ郢晢ｽ｢郢昴・ﾎ・
 * ID 邵ｺ・ｯ String繝ｻ繝ｻnum驕問扱・ｭ・｢繝ｻ繝ｻ */
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
