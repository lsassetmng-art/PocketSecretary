package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import android.widget.Toast;

public final class NotifyGateway {

    // Play螳牙・・壹Θ繝ｼ繧ｶ繝ｼ謫堺ｽ懆ｵｷ轤ｹ縺ｮ縺ｿ
    public static void fire(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
