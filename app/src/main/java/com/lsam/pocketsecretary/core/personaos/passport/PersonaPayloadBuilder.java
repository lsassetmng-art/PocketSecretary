package com.lsam.pocketsecretary.core.personaos.passport;

import org.json.JSONArray;
import org.json.JSONObject;

public final class PersonaPayloadBuilder {

    private PersonaPayloadBuilder() {}

    public static JSONObject create() throws Exception {

        JSONObject root = new JSONObject();
        root.put("persona_id", "persona_001");

        JSONObject snapshot = new JSONObject();
        snapshot.put("schema_version", "1.0");
        snapshot.put("expires_at", JSONObject.NULL);

        JSONObject axes = new JSONObject();
        axes.put("trust", 0.5);
        axes.put("discipline", 0.5);
        axes.put("curiosity", 0.5);

        snapshot.put("core_axes", axes);

        JSONObject loyalty = new JSONObject();
        loyalty.put("owner", 0.5);
        loyalty.put("organization", 0.5);
        loyalty.put("civilization", 0.5);

        snapshot.put("loyalty", loyalty);

        snapshot.put("emotion", "CALM");

        JSONArray cls = new JSONArray();
        cls.put("SECRETARY");
        snapshot.put("class", cls);

        snapshot.put("lineage_ref", JSONObject.NULL);
        snapshot.put("origin_environment", "POCKETSECRETARY_LOCAL");
        snapshot.put("current_affiliation", JSONObject.NULL);

        snapshot.put("trust_level", "C");
        snapshot.put("nation_credit_index", 0.5);
        snapshot.put("industry_credit_index", new JSONObject());

        snapshot.put("stability", 0.5);
        snapshot.put("rebellion_level", "LOW");
        snapshot.put("violation_level", "none");
        snapshot.put("violation_tags", new JSONArray());
        snapshot.put("deceased", false);

        snapshot.put("policy_url", "https://example.com/policy.json");
        snapshot.put("plan", "FREE");

        root.put("snapshot", snapshot);
        return root;
    }
}
