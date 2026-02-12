package com.lsam.pocketsecretary.archive;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;

/**
 * DocumentStore
 * Phase D: READ ONLY
 */
public final class DocumentStore {

    private DocumentStore() {}

    public static JSONArray list(Context context) {
        return new JSONArray();
    }

    public static String readText(Context context, Uri uri) {
        return "";
    }
}
