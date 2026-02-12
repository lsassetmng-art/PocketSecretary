package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import android.widget.Toast;

public final class NotifyGateway {

    // Play陞ｳ迚吶・繝ｻ螢ｹﾎ倡ｹ晢ｽｼ郢ｧ・ｶ郢晢ｽｼ隰ｫ蝣ｺ・ｽ諛・ｽｵ・ｷ霓､・ｹ邵ｺ・ｮ邵ｺ・ｿ
    public static void fire(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
