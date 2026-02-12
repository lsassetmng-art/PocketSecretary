package com.lsam.pocketsecretary.core.secretary;

public class SecretarySpeech {

    public static String greet(Secretary s) {
        if ("ren".equals(s.id)) return "莠亥ｮ壹□縺代∫｢ｺ隱阪＠繧医≧縲・;
        if ("aoi".equals(s.id)) return "莉頑律縺ｮ莠亥ｮ壹ｒ髱吶°縺ｫ謨ｴ縺医∪縺吶・;
        return "縺翫・繧医≧縲ゆｻ頑律繧ゆｸ邱偵↓謨ｴ縺医ｈ縺・・縲・;
    }

    public static String planLine(Secretary s, String nextEventLine) {
        if (nextEventLine == null || nextEventLine.isEmpty()) {
            if ("ren".equals(s.id)) return "谺｡縺ｮ莠亥ｮ壹・譛ｪ逋ｻ骭ｲ縲ょｿ・ｦ√↑繧芽ｿｽ蜉縺励※縲・;
            if ("aoi".equals(s.id)) return "谺｡縺ｮ莠亥ｮ壹・隕九▽縺九ｊ縺ｾ縺帙ｓ縺ｧ縺励◆縲・;
            return "谺｡縺ｮ莠亥ｮ壹・縺ｾ縺辟｡縺・∩縺溘＞縲ょｿ・ｦ√↑繧牙・繧後※縺翫％縺・・;
        }
        if ("ren".equals(s.id)) return "谺｡・・" + nextEventLine;
        if ("aoi".equals(s.id)) return "谺｡縺ｮ莠亥ｮ壹〒縺吶・n" + nextEventLine;
        return "谺｡縺ｮ莠亥ｮ壹・縺薙ｌ縺繧医・n" + nextEventLine;
    }

    public static String notifyText(Secretary s, String base) {
        if ("ren".equals(s.id)) return base;
        if ("aoi".equals(s.id)) return "縺顔衍繧峨○・・" + base;
        return "縺ｭ縺医・ + base;
    }
}
