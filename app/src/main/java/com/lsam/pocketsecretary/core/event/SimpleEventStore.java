package com.lsam.pocketsecretary.core.event;

import android.content.Context;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.UUID;

public class SimpleEventStore {
    private static final String KEY = "simple_events_json";

    private static JSONArray load(Context c) throws Exception {
        return new JSONArray(Prefs.sp(c).getString(KEY, "[]"));
    }

    private static void save(Context c, JSONArray arr) {
        Prefs.sp(c).edit().putString(KEY, arr.toString()).apply();
    }

    public static void add(Context c, String title, long startAt) {
        try {
            JSONArray arr = load(c);
            JSONObject o = new JSONObject();
            o.put("id", UUID.randomUUID().toString());
            o.put("title", title);
            o.put("startAt", startAt);
            arr.put(o);
            save(c, arr);
        } catch (Exception ignored) {}
    }

    public static void update(Context c, String id, String title, long startAt) {
        try {
            JSONArray arr = load(c);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (id.equals(o.getString("id"))) {
                    o.put("title", title);
                    o.put("startAt", startAt);
                    break;
                }
            }
            save(c, arr);
        } catch (Exception ignored) {}
    }

    public static void delete(Context c, String id) {
        try {
            JSONArray arr = load(c);
            JSONArray out = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (!id.equals(o.getString("id"))) out.put(o);
            }
            save(c, out);
        } catch (Exception ignored) {}
    }

    public static JSONArray list(Context c) {
        try { return load(c); } catch (Exception e) { return new JSONArray(); }
    }
}
