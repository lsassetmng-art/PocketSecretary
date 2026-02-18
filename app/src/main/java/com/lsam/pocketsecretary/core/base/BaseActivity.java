package com.lsam.pocketsecretary.core.base;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.core.theme.GlobalTheme;
import com.lsam.pocketsecretary.core.theme.ThemeManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected GlobalTheme globalTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        globalTheme = ThemeManager.resolveGlobalTheme(this);
        super.onCreate(savedInstanceState);
    }

    protected final void applyThemeToRoot(@Nullable View root) {
        if (root == null) return;
        root.setBackgroundColor(globalTheme.backgroundColor);
    }
}
