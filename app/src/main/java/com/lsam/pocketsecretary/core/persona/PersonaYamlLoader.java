package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import com.lsam.pocketsecretary.core.assets.AssetRepository;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public final class PersonaYamlLoader {

    private PersonaYamlLoader() {}

    public static Map<String, String> load(Context context, String personaId) {

        Map<String, String> map = new HashMap<>();

        try {

            // 🔹 AssetRepository経由で統一
            String path = AssetRepository.personaYaml(personaId);
            String yamlText = AssetRepository.loadText(context, path);

            if (yamlText == null) return map;

            BufferedReader reader =
                    new BufferedReader(new StringReader(yamlText));

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

        } catch (Exception e) {
            e.printStackTrace(); // ← ローカル完成フェーズではログ出す

        }

        return map;
    }
}
