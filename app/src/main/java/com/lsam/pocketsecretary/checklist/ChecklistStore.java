package com.lsam.pocketsecretary.checklist;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import org.json.*;

public class ChecklistStore {
    private static final String KEY="checklist_items";
    private static JSONArray load(Context c){
        try{ return new JSONArray(Prefs.sp(c).getString(KEY,"[]")); }
        catch(Exception e){ return new JSONArray(); }
    }
    private static void save(Context c, JSONArray a){
        Prefs.sp(c).edit().putString(KEY,a.toString()).apply();
    }
    public static void add(Context c, String text){
        try{
            JSONArray a=load(c);
            JSONObject o=new JSONObject();
            o.put("text",text);
            o.put("done",false);
            a.put(o); save(c,a);
        }catch(Exception ignored){}
    }
    public static void toggle(Context c, int idx){
        try{
            JSONArray a=load(c);
            JSONObject o=a.getJSONObject(idx);
            o.put("done",!o.getBoolean("done"));
            save(c,a);
        }catch(Exception ignored){}
    }
    public static JSONArray list(Context c){ return load(c); }
}
