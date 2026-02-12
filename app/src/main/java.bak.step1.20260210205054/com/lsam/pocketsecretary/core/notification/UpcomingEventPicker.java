package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.core.repeat.RepeatUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpcomingEventPicker {

    public static class Ev {
        public long at;
        public String title;
        public Ev(long at, String title){ this.at=at; this.title=title; }
    }

    private static void addInternal(Context c, ArrayList<Ev> out){
        long now = System.currentTimeMillis();
        try{
            JSONArray arr = SimpleEventStore.list(c);
            for (int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                String title=o.getString("title");
                long base=o.getLong("startAt");
                String rep=o.optString("repeat", null);
                long at = base;
                if (rep!=null && at < now) at = RepeatUtil.nextAt(at, rep);
                if (at >= now) out.add(new Ev(at, title));
            }
        }catch(Exception ignored){}
    }

    private static void addExternal(Context c, ArrayList<Ev> out){
        long now = System.currentTimeMillis();
        Cursor cur = c.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART},
                CalendarContract.Events.DTSTART + " >= ?",
                new String[]{String.valueOf(now)},
                CalendarContract.Events.DTSTART + " ASC"
        );
        if (cur!=null){
            try{
                int limit = 10; // 鬮ｯ・ｷ繝ｻ・ｿ髫ｰ雋ｻ・ｽ・ｶ郢晢ｽｻ闔ｨ繝ｻ・ｽ・ｩ陷肴ｻゑｽｽ・ｼ繝ｻ・ｱ鬩阪・繝ｻ繝ｻ・ｸ繝ｻ・ｺ郢晢ｽｻ繝ｻ・ｪ鬩搾ｽｵ繝ｻ・ｺ驛｢譎｢・ｽ・ｻ
                while (limit-- > 0 && cur.moveToNext()){
                    String t = cur.getString(0);
                    long at = cur.getLong(1);
                    out.add(new Ev(at, t));
                }
            } finally { cur.close(); }
        }
    }

    public static Ev[] pickTwo(Context c){
        ArrayList<Ev> all = new ArrayList<>();
        addExternal(c, all);
        addInternal(c, all);
        Collections.sort(all, Comparator.comparingLong(e -> e.at));
        if (all.isEmpty()) return new Ev[]{null,null};
        Ev first = all.get(0);
        Ev second = null;
        for (int i=1;i<all.size();i++){
            if (all.get(i).at >= first.at){
                second = all.get(i);
                break;
            }
        }
        return new Ev[]{first, second};
    }
}
