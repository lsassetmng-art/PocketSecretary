package com.lsam.pocketsecretary.core.secretary;

import android.content.Context;

import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SecretaryCatalog {

    private SecretaryCatalog() {}

    public static List<SecretaryInfo> loadAll(Context context) {

        List<SecretaryInfo> list = new ArrayList<>();

        list.add(load(context, "alpha"));
        list.add(load(context, "beta"));
        list.add(load(context, "gamma"));

        return list;
    }

    private static SecretaryInfo load(Context context, String personaId) {

        Map<String, String> map =
                PersonaYamlLoader.load(context, personaId);

        String displayName = map.get("display_name");

        if (displayName == null) {
            displayName = personaId;
        }

        return new SecretaryInfo(personaId, displayName);
    }
}
