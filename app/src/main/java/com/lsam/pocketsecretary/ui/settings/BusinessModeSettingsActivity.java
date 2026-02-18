package com.lsam.pocketsecretary.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.base.BaseActivity;
import com.lsam.pocketsecretary.core.theme.BusinessModeManager;

public class BusinessModeSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_mode_settings);

        View root = findViewById(R.id.rootBusinessMode);
        applyThemeToRoot(root);

        Switch sw = findViewById(R.id.switchBusinessMode);
        sw.setChecked(BusinessModeManager.isEnabled(this));
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BusinessModeManager.setEnabled(BusinessModeSettingsActivity.this, isChecked);
                // Theme is resolved on next Activity creation; keep behavior stable.
                recreate();
            }
        });
    }
}
