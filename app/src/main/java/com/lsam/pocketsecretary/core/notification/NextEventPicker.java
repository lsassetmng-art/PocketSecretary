package com.lsam.pocketsecretary.core.notification;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.lsam.pocketsecretary.core.event.SimpleEventStore;

import org.json.JSONArray;
import org.json.JSONObject;

public class NextEventPicker {

    public static class Picked {
        public long startAt;
        public String title;
    }

    public static Picked pick(Context c) {
        long now = System.currentTimeMillis();
        Picked best = null;

        // ---- external calendar (READ) ----
        Cursor cursor = c.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART },
                CalendarContract.Events.DTSTART + " >= ?",
                new String[]{ String.valueOf(now) },
                CalendarContract.Events.DTSTART + " ASC"
        );
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    best = new Picked();
                    best.title = cursor.getString(0);
                    best.startAt = cursor.getLong(1);
                }
            } finally {
                cursor.close();
            }
        }

        // ---- internal simple events ----
        try {
            JSONArray arr = SimpleEventStore.list(c);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                long startAt = o.getLong("startAt");
                if (startAt < now) continue;

                if (best == null || startAt < best.startAt) {
                    best = new Picked();
                    best.startAt = startAt;
                    best.title = o.getString("title");
                }
            }
        } catch (Exception ignored) {}

        return best;
    }
}
