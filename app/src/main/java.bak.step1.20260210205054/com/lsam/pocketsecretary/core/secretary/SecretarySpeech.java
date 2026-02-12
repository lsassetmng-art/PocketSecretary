package com.lsam.pocketsecretary.core.secretary;

public class SecretarySpeech {

    public static String greet(Secretary s) {
        if ("ren".equals(s.id)) return "闔莠･・ｮ螢ｹ笆｡邵ｺ莉｣ﾂ竏ｫ・｢・ｺ髫ｱ髦ｪ・郢ｧ蛹ｻ竕ｧ邵ｲ繝ｻ;
        if ("aoi".equals(s.id)) return "闔蛾大ｾ狗ｸｺ・ｮ闔莠･・ｮ螢ｹ・帝ｫｱ蜷ｶﾂｰ邵ｺ・ｫ隰ｨ・ｴ邵ｺ蛹ｻ竏ｪ邵ｺ蜷ｶﾂ繝ｻ;
        return "邵ｺ鄙ｫ繝ｻ郢ｧ蛹ｻ竕ｧ邵ｲ繧・ｽｻ鬆大ｾ狗ｹｧ繧・ｽｸﾂ驍ｱ蛛ｵ竊楢ｬｨ・ｴ邵ｺ蛹ｻ・育ｸｺ繝ｻ繝ｻ邵ｲ繝ｻ;
    }

    public static String planLine(Secretary s, String nextEventLine) {
        if (nextEventLine == null || nextEventLine.isEmpty()) {
            if ("ren".equals(s.id)) return "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ繝ｻ隴幢ｽｪ騾具ｽｻ鬪ｭ・ｲ邵ｲ繧・ｽｿ繝ｻ・ｦ竏壺・郢ｧ闃ｽ・ｿ・ｽ陷会｣ｰ邵ｺ蜉ｱ窶ｻ邵ｲ繝ｻ;
            if ("aoi".equals(s.id)) return "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ繝ｻ髫穂ｹ昶命邵ｺ荵晢ｽ顔ｸｺ・ｾ邵ｺ蟶呻ｽ鍋ｸｺ・ｧ邵ｺ蜉ｱ笳・ｸｲ繝ｻ;
            return "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ繝ｻ邵ｺ・ｾ邵ｺ・ｰ霎滂ｽ｡邵ｺ繝ｻ竏ｩ邵ｺ貅假ｼ樒ｸｲ繧・ｽｿ繝ｻ・ｦ竏壺・郢ｧ迚吶・郢ｧ蠕娯ｻ邵ｺ鄙ｫ・・ｸｺ繝ｻﾂ繝ｻ;
        }
        if ("ren".equals(s.id)) return "隹ｺ・｡繝ｻ繝ｻ" + nextEventLine;
        if ("aoi".equals(s.id)) return "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ縲堤ｸｺ蜷ｶﾂ繝ｻn" + nextEventLine;
        return "隹ｺ・｡邵ｺ・ｮ闔莠･・ｮ螢ｹ繝ｻ邵ｺ阮呻ｽ檎ｸｺ・ｰ郢ｧ蛹ｻﾂ繝ｻn" + nextEventLine;
    }

    public static String notifyText(Secretary s, String base) {
        if ("ren".equals(s.id)) return base;
        if ("aoi".equals(s.id)) return "邵ｺ鬘碑｡咲ｹｧ蟲ｨ笳九・繝ｻ" + base;
        return "邵ｺ・ｭ邵ｺ蛹ｻﾂ繝ｻ + base;
    }
}
