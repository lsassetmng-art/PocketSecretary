package com.lsam.pocketsecretary.core.notification;

import android.app.*;
import android.content.*;
import java.util.Calendar;

public class WeeklySummaryScheduler {
    public static void schedule(Context c){
        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY,7);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        if (cal.getTimeInMillis() < System.currentTimeMillis()) cal.add(Calendar.DATE,7);

        Intent i=new Intent(c, NotificationReceiver.class);
        i.setAction("WEEKLY_SUMMARY");
        i.putExtra("title","今週の予定");
        i.putExtra("text","今週の予定を確認しましょう");

        PendingIntent pi=PendingIntent.getBroadcast(
            c,2001,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pi);
    }
}
