package com.lsam.pocketsecretary.core.personaos.passport.cache;

import android.content.Context;
import android.content.SharedPreferences;

public final class PersonaCacheStore {

    private PersonaCacheStore() {}

    private static final String PREF = "persona_passport_cache";

    private static final String K_KEYS_JSON = "keys_current_json";
    private static final String K_KEYS_FETCHED_AT = "keys_current_fetched_at";

    private static final String K_REV_JSON = "revocation_list_json";
    private static final String K_REV_FETCHED_AT = "revocation_list_fetched_at";

    public static void saveKeys(Context ctx, String json, long fetchedAt) {
        sp(ctx).edit()
                .putString(K_KEYS_JSON, json)
                .putLong(K_KEYS_FETCHED_AT, fetchedAt)
                .apply();
    }

    public static String loadKeysJson(Context ctx) {
        return sp(ctx).getString(K_KEYS_JSON, null);
    }

    public static long loadKeysFetchedAt(Context ctx) {
        return sp(ctx).getLong(K_KEYS_FETCHED_AT, 0L);
    }

    public static void saveRevocations(Context ctx, String json, long fetchedAt) {
        sp(ctx).edit()
                .putString(K_REV_JSON, json)
                .putLong(K_REV_FETCHED_AT, fetchedAt)
                .apply();
    }

    public static String loadRevocationsJson(Context ctx) {
        return sp(ctx).getString(K_REV_JSON, null);
    }

    public static long loadRevocationsFetchedAt(Context ctx) {
        return sp(ctx).getLong(K_REV_FETCHED_AT, 0L);
    }

    private static SharedPreferences sp(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }
}
