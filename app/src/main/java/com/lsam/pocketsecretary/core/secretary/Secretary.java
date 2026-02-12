package com.lsam.pocketsecretary.core.secretary;

/**
 * Secretary
 * Phase B 髮弱・・ｽ・｣髫ｴ蟷｢・ｽ・ｬ驛｢譎｢・ｽ・｢驛｢譏ｴ繝ｻ・弱・
 * ID 驍ｵ・ｺ繝ｻ・ｯ String郢晢ｽｻ郢晢ｽｻnum鬩募撫謇ｱ繝ｻ・ｭ繝ｻ・｢郢晢ｽｻ郢晢ｽｻ */
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
