package com.lsam.pocketsecretary.archive;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

public final class ArchiveStore {
    private static final String PREF = "archive_store";
    private static final String KEY  = "items";

    private static SharedPreferences sp(Context c){
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static JSONArray list(Context c){
        try{
            return new JSONArray(sp(c).getString(KEY, "[]"));
        }catch(Exception e){
            return new JSONArray();
        }
    }

    public static void add(Context c, String title){
        try{
            JSONArray a = list(c);
            JSONObject o = new JSONObject();
            o.put("title", title);
            o.put("ts", System.currentTimeMillis());
            a.put(o);
            sp(c).edit().putString(KEY, a.toString()).apply();
        }catch(Exception ignore){}
    }

    public static void clear(Context c){
        sp(c).edit().remove(KEY).apply();
    }
}
