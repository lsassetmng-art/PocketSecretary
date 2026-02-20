package com.lsam.pocketsecretary.core.personaos.passport.verify;

public class PersonaVerifyResult {

    public enum Status {
        OK,
        OFFLINE_USED_CACHE,
        INVALID,
        EXPIRED,
        ERROR
    }

    public final Status status;
    public final String reason;   // e.g. "revoked", "expired", "invalid_signature", "key_compromised", "http_401", "http_429"
    public final String message;  // human readable for UI (already localized by caller if desired)

    public PersonaVerifyResult(Status status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
    }

    public static PersonaVerifyResult ok() {
        return new PersonaVerifyResult(Status.OK, null, null);
    }

    public static PersonaVerifyResult offlineUsedCache() {
        return new PersonaVerifyResult(Status.OFFLINE_USED_CACHE, "offline", null);
    }

    public static PersonaVerifyResult invalid(String reason) {
        return new PersonaVerifyResult(Status.INVALID, reason, null);
    }

    public static PersonaVerifyResult expired() {
        return new PersonaVerifyResult(Status.EXPIRED, "expired", null);
    }

    public static PersonaVerifyResult error(String reason, String message) {
        return new PersonaVerifyResult(Status.ERROR, reason, message);
    }
}
