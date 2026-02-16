package com.lsam.pocketsecretary.core.personaos.visual;

import android.content.Context;
import com.lsam.pocketsecretary.R;

public final class VisualComposeConfig {

    public final String projectRef;
    public final String anonKey;
    public final String outputBucket;
    public final String functionPath;

    private VisualComposeConfig(String p, String k, String b, String f) {
        this.projectRef = p;
        this.anonKey = k;
        this.outputBucket = b;
        this.functionPath = f;
    }

    public static VisualComposeConfig from(Context ctx) {
        return new VisualComposeConfig(
                ctx.getString(R.string.ps_supabase_project_ref).trim(),
                ctx.getString(R.string.ps_supabase_anon_key).trim(),
                ctx.getString(R.string.ps_output_bucket).trim(),
                ctx.getString(R.string.ps_visual_compose_function_path).trim()
        );
    }
}
