package com.lsam.pocketsecretary.core.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.notification.NotificationReceiver;

public final class AutoNotifyScheduler {

    private AutoNotifyScheduler(){}

    // 髫ｴ蟠｢ﾂ髯昴・蠕励・・ｼ陞｢・ｹ遶雁､・ｹ・ｧ驗呻ｽｫ遶包｣ｰ驍ｵ・ｺ陋ｹ・ｻ隨倥・・ｸ・ｲ隴ｴ・ｧ繝ｻ・ｬ繝ｻ・｡髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ隨・ｽｲ驍ｵ・ｺ繝ｻ・ｽ驍ｵ・ｺ郢晢ｽｻ・つ陞溘ｊ・｡蜥ｲ・ｸ・ｲ鬮ｦ・ｪ繝ｻ螳壽ｻ矩密・ｲ繝ｻ・ｴ郢晢ｽｻ繝ｻ・ｼ陜捺ｻ薙◆髫ｴ・ｬ繝ｻ・ｼ鬨ｾ・ｧ郢晢ｽｻ遶企・・ｹ・ｧ繝ｻ・ｫ驛｢譎｢・ｽ・ｬ驛｢譎｢・ｽ・ｳ驛｢謨鳴驛｢譎｢・ｽ・ｼ鬯ｨ・ｾ繝ｻ・｣髯ｷ蟠趣ｽｼ譁舌・髯溷供・ｾ蠕個螳壽ｷｦ繝ｻ・ｮ驍ｵ・ｺ驍・ｽｲ陝蟶ｷ・ｸ・ｺ郢晢ｽｻK郢晢ｽｻ郢晢ｽｻ
    // Play髯橸ｽｳ霑壼生繝ｻ驍ｵ・ｺ繝ｻ・ｮ髫ｴ・ｬ繝ｻ・ｸ郢晢ｽｻ陞｢・ｹ・主｡・ｹ譎｢・ｽ・ｼ驛｢・ｧ繝ｻ・ｶ驛｢譎｢・ｽ・ｼ驍ｵ・ｺ驕偵・ﾂ陞溘ｊ・｡蠑朗驍ｵ・ｺ繝ｻ・ｮ髫ｴ蠑ｱ・・ｹ晢ｽｻ驍ｵ・ｺ繝ｻ・ｿ
    public static void scheduleDemo(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;

        long now = System.currentTimeMillis();
        // 驛｢譏ｴ繝ｻ・取鱒繝ｻ郢晢ｽｻ0髯具ｽｻ郢晢ｽｻ繝ｻ・ｾ陟募ｨｯ繝ｻ1髣比ｼ夲ｽｽ・ｶ郢晢ｽｻ闔・･髯悟､頑割隲帙・・ｽ・｢繝ｻ・ｺ鬮ｫ・ｱ陷･・ｲ騾｡莉｣繝ｻ郢晢ｽｻ
        long at = now + 10L * 60L * 1000L;

        Intent i = new Intent(c, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TITLE, "髣費｣ｰ闔・･繝ｻ・ｮ陞滂ｽｲ繝ｻ・ｼ陞溘ｑ・ｽ・｢繝ｻ・ｺ鬮ｫ・ｱ郢晢ｽｻ);
        i.putExtra(NotificationReceiver.EXTRA_TEXT, "鬯ｨ・ｾ陞溘ｊ・｡蜥ｲ・ｸ・ｺ隰疲ｺｯ蜉帝蘭・ｴ隲帛･・ｽｼ・ｰ驍ｵ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷会ｽｱ隨ｳ繝ｻ繝ｻ陋ｹ・ｻ郢晢ｽｧ驛｢譎｢・ｽ・｢郢晢ｽｻ郢晢ｽｻ);

        PendingIntent pi = PendingIntent.getBroadcast(
                c, 1001, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, at, pi);
    }
}
