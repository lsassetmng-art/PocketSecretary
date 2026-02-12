package com.lsam.pocketsecretary.core.dashboard;

import android.content.Context;
import java.util.List;
import com.lsam.pocketsecretary.core.event.Event;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;

public final class TodayEngine {

    private TodayEngine(){}

    public static int todayCount(Context context){
        return SimpleEventStore.countToday(context);
    }

    public static String nextTitle(Context context){
        List<Event> list = SimpleEventStore.list(context);
        if(list == null || list.isEmpty()) return "-";

        long now = System.currentTimeMillis();
        Event best = null;

        for(Event e : list){
            if(e == null) continue;
            if(e.time < now) continue;
            if(best == null || e.time < best.time) best = e;
        }

        if(best == null) return "-";
        return (best.title == null || best.title.isEmpty()) ? "-" : best.title;
    }

    public static boolean isTight(Context context){
        return todayCount(context) >= 5;
    }
}
