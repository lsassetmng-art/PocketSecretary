package com.lsam.pocketsecretary.archive;

import android.content.Context;
import android.net.Uri;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.UUID;

public class DocumentStore {
    private static final String KEY = "archive_docs_json";

    private static JSONArray load(Context c) throws Exception {
        return new JSONArray(Prefs.sp(c).getString(KEY, "[]"));
    }
    private static void save(Context c, JSONArray arr){
        Prefs.sp(c).edit().putString(KEY, arr.toString()).apply();
    }

    public static void add(Context c, String title, String category, String note, Uri uri){
        try{
            JSONArray arr = load(c);
            JSONObject o = new JSONObject();
            o.put("id", UUID.randomUUID().toString());
            o.put("title", title);
            o.put("category", category);
            o.put("note", note==null?"":note);
            o.put("uri", uri.toString());
            o.put("ts", System.currentTimeMillis());
            arr.put(o);
            save(c, arr);
        }catch(Exception ignored){}
    }

    public static JSONArray list(Context c){
        try{ return load(c); }catch(Exception e){ return new JSONArray(); }
    }
}
