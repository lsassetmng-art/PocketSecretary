package com.lsam.pocketsecretary.persona;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YamlPersonaLoader {

    public static PersonaModel load(Context context) {

        String name = "Secretary";
        String prefix = "";
        String suffix = "";
        String emotion = "calm";

        try {
            InputStream is = context.getAssets().open("persona.yaml");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("name:"))
                    name = line.replace("name:", "").trim();

                if (line.startsWith("prefix:"))
                    prefix = line.replace("prefix:", "").trim();

                if (line.startsWith("suffix:"))
                    suffix = line.replace("suffix:", "").trim();

                if (line.startsWith("emotion:"))
                    emotion = line.replace("emotion:", "").trim();
            }

            br.close();

        } catch (Exception ignored) {}

        return new PersonaModel(name, prefix, suffix, emotion);
    }
}