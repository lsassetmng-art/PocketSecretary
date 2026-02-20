package com.lsam.pocketsecretary.core.personaos.passport.verify;

public final class PersonaPassportEndpoints {

    private PersonaPassportEndpoints() {}

    public static final String KEYS_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/keys-current";

    public static final String REVOCATION_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/revocation-list";

    public static final String VERIFY_URL =
            "https://ihrukfdlcolygyvccujd.functions.supabase.co/snapshot-verify";
}
