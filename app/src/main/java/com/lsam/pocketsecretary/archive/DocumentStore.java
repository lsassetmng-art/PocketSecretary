package com.lsam.pocketsecretary.archive;

import android.content.Context;
import android.net.Uri;
import org.json.JSONArray;

public class DocumentStore {

    public static JSONArray list(Context ctx) {
        return new JSONArray();
    }

    public static void add(Context ctx, String name, String desc, String tag, Uri uri) {
        // no-op Phase A
    }
}