package com.lsam.pocketsecretary.persona;

import android.content.Context;
import com.lsam.pocketsecretary.R;

public class DefaultPersona implements SecretaryPersona {

    private final PersonaModel model;
    private final Context context;

    public DefaultPersona(Context context, PersonaModel model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public String format(String message) {

        String emotionPrefix = "";

        if ("alert".equals(model.emotion)) {
            emotionPrefix = context.getString(R.string.prefix_alert);
        }

        if ("speaking".equals(model.emotion)) {
            emotionPrefix = context.getString(R.string.prefix_speaking);
        }

        return emotionPrefix + model.prefix + message + model.suffix;
    }

    @Override
    public String getName() {
        return model.name;
    }
}
