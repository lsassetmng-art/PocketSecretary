package com.lsam.pocketsecretary.log;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import org.json.*;

public class SecretaryLog {
    private static final String KEY="sec_log";
    private static JSONArray load(Context c){
        try{ return new JSONArray(Prefs.sp(c).getString(KEY,"[]")); }
        catch(Exception e){ return new JSONArray(); }
    }
    private static void save(Context c, JSONArray a){
        Prefs.sp(c).edit().putString(KEY,a.toString()).apply();
    }
    public static void add(Context c, String msg){
        try{
            JSONArray a=load(c);
            JSONObject o=new JSONObject();
            o.put("ts",System.currentTimeMillis());
            o.put("msg",msg);
            a.put(o); save(c,a);
        }catch(Exception ignored){}
    }
    public static JSONArray list(Context c){ return load(c); }
}
