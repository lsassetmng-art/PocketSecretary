package com.lsam.pocketsecretary.core.persona;

import android.content.Context;

import com.lsam.pocketsecretary.core.assets.AssetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonaMetaLoader {

    public static class PersonaMeta {
        public String personaId;
        public String displayName;

        // Canonical v1.1（persona.yaml から取得）
        public String requiredVisualPackId;
        public String requiredBackgroundPackId;
        public int minVisualRevision;

        // 既存UI互換のため残す（SkinStore等が参照）
        public String defaultSkin;
        public List<String> availableSkins;

        public String defaultVoice;
        public List<String> availableVoices;

        public int assetVersion;
    }

    public static PersonaMeta load(Context context, String personaId) {
        try {
            // runtime/persona/{persona_id}/persona.yaml を読む（正本）
            String yamlPath = AssetRepository.personaYaml(personaId);
            String yamlText = AssetRepository.loadText(context, yamlPath);
            if (yamlText == null) return null;

            // 既存の PersonaYamlLoader を活用（Mapで受ける）
            Map<String, String> map = PersonaYamlLoader.load(context, personaId);
            if (map == null) return null;

            PersonaMeta meta = new PersonaMeta();
            meta.personaId = personaId;

            String displayName = map.get("display_name");
            meta.displayName = (displayName != null && !displayName.isEmpty())
                    ? displayName
                    : personaId;

            // persona.yaml canonical keys
            meta.requiredVisualPackId = safe(map.get("required_visual_pack_id"));
            meta.requiredBackgroundPackId = safe(map.get("required_background_pack_id"));

            String minRev = map.get("min_visual_revision");
            meta.minVisualRevision = parseIntOr(minRev, 1);

            // 互換（SkinStore のデフォルトに使うだけ）
            meta.defaultSkin = "default";
            meta.availableSkins = new ArrayList<>();

            meta.defaultVoice = "default";
            meta.availableVoices = new ArrayList<>();

            // 将来の asset_version は persona.yaml or manifest へ寄せるが、今は固定
            meta.assetVersion = 1;

            return meta;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static int parseIntOr(String s, int def) {
        try {
            if (s == null) return def;
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
