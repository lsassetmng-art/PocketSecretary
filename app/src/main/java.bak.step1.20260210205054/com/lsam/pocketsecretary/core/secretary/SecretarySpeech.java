package com.lsam.pocketsecretary.core.secretary;

public class SecretarySpeech {

    public static String greet(Secretary s) {
        if ("ren".equals(s.id)) return "髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ隨・ｽ｡驍ｵ・ｺ闔会ｽ｣・つ遶擾ｽｫ繝ｻ・｢繝ｻ・ｺ鬮ｫ・ｱ鬮ｦ・ｪ繝ｻ・ｰ驛｢・ｧ陋ｹ・ｻ遶包ｽｧ驍ｵ・ｲ郢晢ｽｻ;
        if ("aoi".equals(s.id)) return "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ繝ｻ蟶晢ｽｫ・ｱ陷ｷ・ｶ・ゑｽｰ驍ｵ・ｺ繝ｻ・ｫ髫ｰ・ｨ繝ｻ・ｴ驍ｵ・ｺ陋ｹ・ｻ遶擾ｽｪ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
        return "驍ｵ・ｺ驗呻ｽｫ郢晢ｽｻ驛｢・ｧ陋ｹ・ｻ遶包ｽｧ驍ｵ・ｲ郢ｧ繝ｻ・ｽ・ｻ鬯・､ｧ・ｾ迢暦ｽｹ・ｧ郢ｧ繝ｻ・ｽ・ｸ・つ鬩搾ｽｱ陋幢ｽｵ遶頑･｢・ｬ・ｨ繝ｻ・ｴ驍ｵ・ｺ陋ｹ・ｻ繝ｻ閧ｲ・ｸ・ｺ郢晢ｽｻ郢晢ｽｻ驍ｵ・ｲ郢晢ｽｻ;
    }

    public static String planLine(Secretary s, String nextEventLine) {
        if (nextEventLine == null || nextEventLine.isEmpty()) {
            if ("ren".equals(s.id)) return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ髫ｴ蟷｢・ｽ・ｪ鬨ｾ蜈ｷ・ｽ・ｻ鬯ｪ・ｭ繝ｻ・ｲ驍ｵ・ｲ郢ｧ繝ｻ・ｽ・ｿ郢晢ｽｻ繝ｻ・ｦ遶丞｣ｺ繝ｻ驛｢・ｧ髣・ｽｽ繝ｻ・ｿ繝ｻ・ｽ髯ｷ莨夲ｽ｣・ｰ驍ｵ・ｺ陷会ｽｱ遯ｶ・ｻ驍ｵ・ｲ郢晢ｽｻ;
            if ("aoi".equals(s.id)) return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ鬮ｫ遨ゑｽｹ譏ｶ蜻ｽ驍ｵ・ｺ闕ｵ譎｢・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｺ繝ｻ・ｧ驍ｵ・ｺ陷会ｽｱ隨ｳ繝ｻ・ｸ・ｲ郢晢ｽｻ;
            return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ驍ｵ・ｺ繝ｻ・ｾ驍ｵ・ｺ繝ｻ・ｰ髴取ｻゑｽｽ・｡驍ｵ・ｺ郢晢ｽｻ遶擾ｽｩ驍ｵ・ｺ雋・∞・ｼ讓抵ｽｸ・ｲ郢ｧ繝ｻ・ｽ・ｿ郢晢ｽｻ繝ｻ・ｦ遶丞｣ｺ繝ｻ驛｢・ｧ霑壼生繝ｻ驛｢・ｧ陟募ｨｯﾂ・ｻ驍ｵ・ｺ驗呻ｽｫ繝ｻ繝ｻ・ｸ・ｺ郢晢ｽｻ・つ郢晢ｽｻ;
        }
        if ("ren".equals(s.id)) return "髫ｹ・ｺ繝ｻ・｡郢晢ｽｻ郢晢ｽｻ" + nextEventLine;
        if ("aoi".equals(s.id)) return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ邵ｲ蝣､・ｸ・ｺ陷ｷ・ｶ・つ郢晢ｽｻn" + nextEventLine;
        return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ驍ｵ・ｺ髦ｮ蜻ｻ・ｽ讙趣ｽｸ・ｺ繝ｻ・ｰ驛｢・ｧ陋ｹ・ｻ・つ郢晢ｽｻn" + nextEventLine;
    }

    public static String notifyText(Secretary s, String base) {
        if ("ren".equals(s.id)) return base;
        if ("aoi".equals(s.id)) return "驍ｵ・ｺ鬯倡｢托ｽ｡蜥ｲ・ｹ・ｧ陝ｲ・ｨ隨ｳ荵昴・郢晢ｽｻ" + base;
        return "驍ｵ・ｺ繝ｻ・ｭ驍ｵ・ｺ陋ｹ・ｻ・つ郢晢ｽｻ + base;
    }
}
