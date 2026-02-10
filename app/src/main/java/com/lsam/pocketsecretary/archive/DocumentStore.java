package com.lsam.pocketsecretary.archive;

import android.content.Context;
import android.net.Uri;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import org.json.*;
import java.util.UUID;

public class DocumentStore {
    private static final String KEY="archive_docs";

    private static JSONArray load(Context c){
        try{ return new JSONArray(Prefs.sp(c).getString(KEY,"[]")); }
        catch(Exception e){ return new JSONArray(); }
    }
    private static void save(Context c, JSONArray a){
        Prefs.sp(c).edit().putString(KEY,a.toString()).apply();
    }

    public static void add(Context c,String title,String cat,Uri uri){
        try{
            JSONArray a=load(c);
            JSONObject o=new JSONObject();
            o.put("id",UUID.randomUUID().toString());
            o.put("title",title);
            o.put("category",cat);
            o.put("uri",uri.toString());
            a.put(o); save(c,a);
        }catch(Exception ignored){}
    }

    public static JSONArray list(Context c){ return load(c); }
}
