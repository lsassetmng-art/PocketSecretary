package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.notification.NotificationReceiver;

public final class AutoNotifyScheduler {

    private AutoNotifyScheduler(){}

    // 譛蟆擾ｼ壹→繧翫≠縺医★縲梧ｬ｡莠亥ｮ壹▲縺ｽ縺・夂衍縲阪ｒ莠育ｴ・ｼ域悽譬ｼ逧・↑繧ｫ繝ｬ繝ｳ繝繝ｼ騾｣蜍輔・蠕後〒蟾ｮ縺玲崛縺・K・・
    // Play螳牙・縺ｮ譬ｸ・壹Θ繝ｼ繧ｶ繝ｼ縺碁夂衍ON縺ｮ譎ゅ・縺ｿ
    public static void scheduleDemo(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long now = System.currentTimeMillis();
        // 繝・Δ・・0蛻・ｾ後↓1莉ｶ・亥虚菴懃｢ｺ隱咲畑・・
        long at = now + 10L * 60L * 1000L;

        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, "莠亥ｮ夲ｼ夂｢ｺ隱・);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, "騾夂衍縺悟虚菴懊＠縺ｾ縺励◆・医ョ繝｢・・);

        PendingIntent pi = PendingIntent.getBroadcast(
                c, 1001, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, at, pi);
    }
}
