package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class PersonaYamlLoader {

    private PersonaYamlLoader() {}

    public static Map<String, String> load(Context context, String personaId) {
        Map<String, String> map = new HashMap<>();

        try {
            InputStream is = context.getAssets()
                    .open("persona/" + personaId + "/persona.yaml");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) continue;
                if (line.startsWith("#")) continue;

                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    map.put(key, value);
                }
            }

            reader.close();

        } catch (Exception ignored) {
        }

        return map;
    }
}