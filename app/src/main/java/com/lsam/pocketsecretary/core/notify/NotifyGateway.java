package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import android.widget.Toast;

public final class NotifyGateway {

    // Play髯橸ｽｳ霑壼生繝ｻ郢晢ｽｻ陞｢・ｹ・主｡・ｹ譎｢・ｽ・ｼ驛｢・ｧ繝ｻ・ｶ驛｢譎｢・ｽ・ｼ髫ｰ・ｫ陜｣・ｺ繝ｻ・ｽ隲帙・・ｽ・ｵ繝ｻ・ｷ髴難ｽ､繝ｻ・ｹ驍ｵ・ｺ繝ｻ・ｮ驍ｵ・ｺ繝ｻ・ｿ
    public static void fire(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
