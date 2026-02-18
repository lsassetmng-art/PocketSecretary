package com.lsam.pocketsecretary.core.theme;

import android.content.Context;
import android.graphics.Color;

public final class ThemeManager {

    // BusinessMode colors (fixed)
    private static final int B_BG = 0xFF0F1E2E;
    private static final int B_SURFACE = 0xFF16283D;
    private static final int B_TEXT_PRIMARY = 0xFFFFFFFF;
    private static final int B_TEXT_SECONDARY = 0xFFB8C7D9;
    private static final int B_BORDER = 0xFF2A3F5F;
    private static final int B_WARNING = 0xFFD98A8A;

    private ThemeManager() {}

    public static GlobalTheme resolveGlobalTheme(Context context) {
        if (BusinessModeManager.isEnabled(context)) {
            return new GlobalTheme(
                    B_BG, B_SURFACE, B_TEXT_PRIMARY, B_TEXT_SECONDARY, B_BORDER, B_WARNING, 4f
            );
        }

        // Persona-derived (subtle, no gradients)
        int accent = PersonaProvider.getAccentColor(context);
        String tone = PersonaProvider.getToneType(context);

        int bg = mix(Color.WHITE, accent, 0.12f);     // 10-15% accent
        int surface = mix(Color.WHITE, accent, 0.08f);

        float radius = 8f;
        if ("tone_soft".equals(tone)) radius = 16f;
        if ("tone_sharp".equals(tone)) radius = 4f;

        return new GlobalTheme(
                bg,
                surface,
                0xFF222222,
                0xFF666666,
                0xFFDDDDDD,
                0xFFB00020,
                radius
        );
    }

    public static CalculatorTheme resolveCalculatorTheme(Context context) {
        GlobalTheme base = resolveGlobalTheme(context);
        int accent = BusinessModeManager.isEnabled(context)
                ? B_BORDER
                : PersonaProvider.getAccentColor(context);
        return new CalculatorTheme(base, accent);
    }

    public static TodoTheme resolveTodoTheme(Context context) {
        return new TodoTheme(resolveGlobalTheme(context));
    }

    private static int mix(int a, int b, float bRatio) {
        bRatio = clamp01(bRatio);
        float aRatio = 1f - bRatio;

        int ar = (a >> 16) & 0xFF;
        int ag = (a >> 8) & 0xFF;
        int ab = a & 0xFF;

        int br = (b >> 16) & 0xFF;
        int bg = (b >> 8) & 0xFF;
        int bb = b & 0xFF;

        int r = Math.round(ar * aRatio + br * bRatio);
        int g = Math.round(ag * aRatio + bg * bRatio);
        int bl = Math.round(ab * aRatio + bb * bRatio);

        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }

    private static float clamp01(float v) {
        if (v < 0f) return 0f;
        if (v > 1f) return 1f;
        return v;
    }
}
