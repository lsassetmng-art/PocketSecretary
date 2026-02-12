package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.core.repeat.RepeatUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class NextEventPicker {
    public static class Picked { public long startAt; public String title; }

    public static Picked pick(Context c){
        long now = System.currentTimeMillis();
        Picked best = null;

        // External (READ)
        Cursor cur = c.getContentResolver().query(
            CalendarContract.Events.CONTENT_URI,
            new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART},
            CalendarContract.Events.DTSTART + " >= ?",
            new String[]{String.valueOf(now)},
            CalendarContract.Events.DTSTART + " ASC"
        );
        if (cur!=null){
            try{
                if (cur.moveToFirst()){
                    best = new Picked();
                    best.title = cur.getString(0);
                    best.startAt = cur.getLong(1);
                }
            } finally { cur.close(); }
        }

        // Internal (repeat aware)
        try{
            JSONArray arr = SimpleEventStore.listAsJson(c);
            for (int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                long base = o.getLong("startAt");
                String rep = o.optString("repeat", null);
                long at = base < now && rep!=null ? RepeatUtil.nextAt(base, rep) : base;
                if (at < now) continue;
                if (best==null || at < best.startAt){
                    best = new Picked();
                    best.startAt = at;
                    best.title = o.getString("title");
                }
            }
        } catch (Exception ignored){}
        return best;
    }
}
