package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import android.widget.Toast;

public final class NotifyGateway {

    // Play安全：ユーザー操作起点のみ
    public static void fire(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
