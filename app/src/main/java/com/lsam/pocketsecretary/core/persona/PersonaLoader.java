package com.lsam.pocketsecretary.core.persona;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public final class PersonaLoader {

    private PersonaLoader() {}

    public static Bitmap loadPersonaImage(Context context, String personaId) {
        try {
            InputStream is = context.getAssets()
                    .open("persona/" + personaId + "/image.png");
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
    }
}
