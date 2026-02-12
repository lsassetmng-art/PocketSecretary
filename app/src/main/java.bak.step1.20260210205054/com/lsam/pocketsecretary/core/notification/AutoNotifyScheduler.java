package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.notification.NotificationReceiver;

public final class AutoNotifyScheduler {

    private AutoNotifyScheduler(){}

    // 隴崢陝・得・ｼ螢ｹ竊堤ｹｧ鄙ｫ竕邵ｺ蛹ｻ笘・ｸｲ譴ｧ・ｬ・｡闔莠･・ｮ螢ｹ笆ｲ邵ｺ・ｽ邵ｺ繝ｻﾂ螟り｡咲ｸｲ髦ｪ・定滋閧ｲ・ｴ繝ｻ・ｼ蝓滓た隴ｬ・ｼ騾ｧ繝ｻ竊醍ｹｧ・ｫ郢晢ｽｬ郢晢ｽｳ郢敖郢晢ｽｼ鬨ｾ・｣陷崎ｼ斐・陟募ｾ後定淦・ｮ邵ｺ邇ｲ蟠帷ｸｺ繝ｻK繝ｻ繝ｻ
    // Play陞ｳ迚吶・邵ｺ・ｮ隴ｬ・ｸ繝ｻ螢ｹﾎ倡ｹ晢ｽｼ郢ｧ・ｶ郢晢ｽｼ邵ｺ遒・螟り｡弘N邵ｺ・ｮ隴弱ｅ繝ｻ邵ｺ・ｿ
    public static void scheduleDemo(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long now = System.currentTimeMillis();
        // 郢昴・ﾎ斐・繝ｻ0陋ｻ繝ｻ・ｾ蠕娯・1闔会ｽｶ繝ｻ莠･陌夊抄諛・ｽ｢・ｺ髫ｱ蜥ｲ逡代・繝ｻ
        long at = now + 10L * 60L * 1000L;

        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, "闔莠･・ｮ螟ｲ・ｼ螟ゑｽ｢・ｺ髫ｱ繝ｻ);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, "鬨ｾ螟り｡咲ｸｺ謔溯劒闖ｴ諛奇ｼ邵ｺ・ｾ邵ｺ蜉ｱ笳・・蛹ｻ繝ｧ郢晢ｽ｢繝ｻ繝ｻ);

        PendingIntent pi = PendingIntent.getBroadcast(
                c, 1001, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, at, pi);
    }
}
