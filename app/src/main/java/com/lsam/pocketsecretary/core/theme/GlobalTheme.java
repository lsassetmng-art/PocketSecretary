package com.lsam.pocketsecretary.core.theme;

public final class GlobalTheme {

    public final int backgroundColor;
    public final int surfaceColor;
    public final int textPrimary;
    public final int textSecondary;
    public final int borderColor;
    public final int warningColor;
    public final float buttonCornerRadiusDp;

    public GlobalTheme(
            int backgroundColor,
            int surfaceColor,
            int textPrimary,
            int textSecondary,
            int borderColor,
            int warningColor,
            float buttonCornerRadiusDp
    ) {
        this.backgroundColor = backgroundColor;
        this.surfaceColor = surfaceColor;
        this.textPrimary = textPrimary;
        this.textSecondary = textSecondary;
        this.borderColor = borderColor;
        this.warningColor = warningColor;
        this.buttonCornerRadiusDp = buttonCornerRadiusDp;
    }
}
