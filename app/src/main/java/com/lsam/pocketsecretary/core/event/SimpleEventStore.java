package com.lsam.pocketsecretary.core.event;

import android.content.Context;

import com.lsam.pocketsecretary.core.prefs.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class SimpleEventStore {

    private static final String KEY = "simple_events_json";

    public static void add(Context c, String title, long startAt) {
        try {
            String raw = Prefs.sp(c).getString(KEY, "[]");
            JSONArray arr = new JSONArray(raw);

            JSONObject o = new JSONObject();
            o.put("id", UUID.randomUUID().toString());
            o.put("title", title);
            o.put("startAt", startAt);

            arr.put(o);
            Prefs.sp(c).edit().putString(KEY, arr.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static JSONArray list(Context c) {
        try {
            String raw = Prefs.sp(c).getString(KEY, "[]");
            return new JSONArray(raw);
        } catch (Exception e) {
            return new JSONArray();
        }
    }
}
