package com.lsam.pocketsecretary.core.notification;

import android.app.*;
import android.content.*;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class AutoNotifyScheduler {

    private static int[] offsets(Context c){
        int[] o = new int[3]; int i=0;
        if (Prefs.getBool(c,"n30",true)) o[i++]=30;
        if (Prefs.getBool(c,"n10",true)) o[i++]=10;
        if (Prefs.getBool(c,"n5", true)) o[i++]=5;
        int[] r = new int[i];
        System.arraycopy(o,0,r,0,i);
        return r;
    }

    public static void rescheduleNext(Context c) {
        if (!Prefs.isNotifyEnabled(c)) return;
        NextEventPicker.Picked ev = NextEventPicker.pick(c);
        if (ev==null) return;

        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        long now=System.currentTimeMillis();
        for (int m: offsets(c)) {
            long at=ev.startAt-m*60_000L;
            if (at<=now) continue;
            Intent i=new Intent(c,NotificationReceiver.class);
            i.putExtra("title","予定のリマインド");
            i.putExtra("text",m+"分後に予定があります: "+ev.title);
            PendingIntent pi=PendingIntent.getBroadcast(
                c,(int)(at%Integer.MAX_VALUE),i,
                PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi);
        }
    }
}
