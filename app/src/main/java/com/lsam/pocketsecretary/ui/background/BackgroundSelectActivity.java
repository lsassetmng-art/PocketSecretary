package com.lsam.pocketsecretary.ui.background;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.settings.BackgroundStore;

public class BackgroundSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_background_select);

        wireButtons();
    }

    private void wireButtons() {

        Button btnDesk = findViewById(R.id.btnDeskSet);
        if (btnDesk != null) {
            btnDesk.setOnClickListener(v -> {
                BackgroundStore.set(this, "desk_set_001");
                finish();
            });
        }

        Button btnDefault = findViewById(R.id.btnDefaultSet);
        if (btnDefault != null) {
            btnDefault.setOnClickListener(v -> {
                BackgroundStore.set(this, "default");
                finish();
            });
        }
    }

    @Override
    protected String getHeaderTitle() {
        return "背景変更";
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
