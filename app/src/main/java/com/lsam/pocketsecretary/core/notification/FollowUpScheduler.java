package com.lsam.pocketsecretary.core.notification;

import android.app.*;
import android.content.*;

public class FollowUpScheduler {
    public static void schedule(Context c, long startAt, String title){
        long at = startAt + 5*60_000L;
        if (at <= System.currentTimeMillis()) return;

        Intent i=new Intent(c, NotificationReceiver.class);
        i.setAction("FOLLOW_UP");
        i.putExtra("title","莠亥ｮ壹′蟋九∪縺｣縺ｦ縺・∪縺・);
        i.putExtra("text","蠢倥ｌ縺ｦ縺・∪縺帙ｓ縺具ｼ・"+title);

        PendingIntent pi=PendingIntent.getBroadcast(
            c,(int)(at%Integer.MAX_VALUE),i,
            PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi);
    }
}
