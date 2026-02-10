package com.lsam.pocketsecretary.core.notification;

import android.app.*;
import android.content.*;
import java.util.Calendar;

public class DailySummaryScheduler {
    public static void schedule(Context c){
        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,7);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        if (cal.getTimeInMillis() < System.currentTimeMillis()) cal.add(Calendar.DATE,1);

        Intent i=new Intent(c,NotificationReceiver.class);
        i.setAction("DAILY_SUMMARY");
        i.putExtra("title","莉頑律縺ｮ莠亥ｮ・);
        i.putExtra("text","莉頑律縺ｮ莠亥ｮ壹ｒ遒ｺ隱阪＠縺ｾ縺励ｇ縺・);

        PendingIntent pi=PendingIntent.getBroadcast(
            c,1001,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);
    }
}
