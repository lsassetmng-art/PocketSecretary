package com.lsam.pocketsecretary.core.personaos.passport;

import android.content.Context;
import org.json.JSONObject;

public final class PersonaSnapshotStore {

    private static final String PREF = "persona_passport";
    private static final String KEY = "latest_snapshot_json";

    private PersonaSnapshotStore() {}

    public static void save(Context ctx, JSONObject snapshot) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY, snapshot.toString())
                .apply();
    }

    public static JSONObject load(Context ctx) {
        try {
            String s = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                    .getString(KEY, null);
            return s == null ? null : new JSONObject(s);
        } catch (Exception e) {
            return null;
        }
    }
}
