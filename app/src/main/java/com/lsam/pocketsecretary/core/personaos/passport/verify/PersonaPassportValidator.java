package com.lsam.pocketsecretary.core.personaos.passport.verify;

import android.content.Context;

import com.lsam.pocketsecretary.core.personaos.passport.cache.PersonaCacheStore;

import org.json.JSONObject;

public final class PersonaPassportValidator {

    private PersonaPassportValidator() {}

    // TTL (ms)
    private static final long TTL_KEYS_MS = 24L * 60L * 60L * 1000L;  // 24h
    private static final long TTL_REV_MS  = 10L * 60L * 1000L;        // 10min

    public static void syncCachesIfNeeded(Context ctx) {
        // オンラインでのみ更新。失敗しても落とさない。
        if (!NetUtil.isOnline(ctx)) return;

        long now = System.currentTimeMillis();

        // keys-current
        try {
            long fetchedAt = PersonaCacheStore.loadKeysFetchedAt(ctx);
            if (now - fetchedAt > TTL_KEYS_MS || PersonaCacheStore.loadKeysJson(ctx) == null) {
                JSONObject keys = PersonaPassportHttp.getJson(PersonaPassportEndpoints.KEYS_URL);
                PersonaCacheStore.saveKeys(ctx, keys.toString(), now);
            }
        } catch (Exception ignored) {}

        // revocation-list
        try {
            long fetchedAt = PersonaCacheStore.loadRevocationsFetchedAt(ctx);
            if (now - fetchedAt > TTL_REV_MS || PersonaCacheStore.loadRevocationsJson(ctx) == null) {
                JSONObject rev = PersonaPassportHttp.getJson(PersonaPassportEndpoints.REVOCATION_URL);
                PersonaCacheStore.saveRevocations(ctx, rev.toString(), now);
            }
        } catch (Exception ignored) {}
    }

    public static PersonaVerifyResult validateSnapshotJson(Context ctx, String snapshotJson) {

        if (snapshotJson == null || snapshotJson.trim().isEmpty()) {
            return PersonaVerifyResult.invalid("missing_snapshot");
        }

        // 期限チェック（ローカル二重防御）
        try {
            JSONObject snap = new JSONObject(snapshotJson);
            if (isExpired(snap)) {
                return PersonaVerifyResult.expired();
            }
        } catch (Exception e) {
            return PersonaVerifyResult.invalid("malformed_snapshot");
        }

        // オフラインなら verify はできない → キャッシュ利用扱い
        if (!NetUtil.isOnline(ctx)) {
            return PersonaVerifyResult.offlineUsedCache();
        }

        // サーバー verify
        try {
            JSONObject body = new JSONObject();
            // 仕様: { "snapshot": <snapshotJson> } だが snapshotJsonが文字列の場合があるので JSONObject化して入れる
            JSONObject snapObj = new JSONObject(snapshotJson);
            body.put("snapshot", snapObj);

            JSONObject resp = PersonaPassportHttp.postJson(PersonaPassportEndpoints.VERIFY_URL, body);

            boolean valid = resp.optBoolean("valid", false);
            if (!valid) {
                // reason があれば拾う
                String reason = resp.optString("reason", "invalid");
                return PersonaVerifyResult.invalid(reason);
            }

            return PersonaVerifyResult.ok();

        } catch (Exception e) {
            // HTTP_401 / HTTP_429 を reason 化
            String msg = e.getMessage() != null ? e.getMessage() : "error";
            String reason = "error";
            if (msg.startsWith("HTTP_401")) reason = "http_401";
            else if (msg.startsWith("HTTP_429")) reason = "http_429";
            return PersonaVerifyResult.error(reason, msg);
        }
    }

    private static boolean isExpired(JSONObject snap) {
        // snapshot の expires_at は null or ISO or millis の可能性がある。
        // Phase1では「Long millis」を優先対応。文字列の場合は無理に解釈せず「期限判定しない」。
        Object ex = snap.opt("expires_at");
        if (ex == null || ex == JSONObject.NULL) return false;

        long now = System.currentTimeMillis();
        if (ex instanceof Number) {
            long ms = ((Number) ex).longValue();
            return now > ms;
        }
        // 文字列の場合は判定不能として false（verify側で判定される想定）
        return false;
    }
}
