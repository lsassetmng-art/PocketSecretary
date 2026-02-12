package com.lsam.pocketsecretary.persona;

import android.content.Context;

public class PersonaRegistry {

    private static SecretaryPersona active;

    public static SecretaryPersona get(Context context) {
        if (active == null) {
            PersonaModel model = YamlPersonaLoader.load(context);
            active = new DefaultPersona(context, model);
        }
        return active;
    }
}