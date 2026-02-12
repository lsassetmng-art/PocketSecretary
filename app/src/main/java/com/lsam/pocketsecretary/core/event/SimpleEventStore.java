package com.lsam.pocketsecretary.core.event;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class SimpleEventStore {

    private static final List<Event> EVENTS = new ArrayList<>();

    public static void add(Context context, String title, long time, String note){
        EVENTS.add(new Event(title, time, note));
    }

    public static List<Event> list(Context context){
        return new ArrayList<>(EVENTS);
    }

    public static JSONArray listAsJson(Context context){
        JSONArray arr = new JSONArray();
        try{
            for(Event e : EVENTS){
                JSONObject o = new JSONObject();
                o.put("title", e.title);
                o.put("time", e.time);
                o.put("note", e.note);
                arr.put(o);
            }
        }catch(Exception ignored){}
        return arr;
    }

    public static int countToday(Context context){
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY,0);
        start.set(Calendar.MINUTE,0);
        start.set(Calendar.SECOND,0);

        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DAY_OF_MONTH,1);

        int c = 0;
        for(Event e : EVENTS){
            if(e.time >= start.getTimeInMillis() && e.time < end.getTimeInMillis()){
                c++;
            }
        }
        return c;
    }
}
