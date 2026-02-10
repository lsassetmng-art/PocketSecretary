package com.lsam.centergravitypuzzle;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {
    private static final String PREF = "cgp_settings";
    private static final String KEY_SE = "se";
    private static final String KEY_VIB = "vib";

    private final SharedPreferences sp;

    public AppSettings(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public boolean isSeEnabled() { return sp.getBoolean(KEY_SE, true); }
    public boolean isVibrationEnabled() { return sp.getBoolean(KEY_VIB, true); }

    public void setSeEnabled(boolean v) { sp.edit().putBoolean(KEY_SE, v).apply(); }
    public void setVibrationEnabled(boolean v) { sp.edit().putBoolean(KEY_VIB, v).apply(); }
}
