// =========================================================
// app/src/main/java/com/lsam/pocketsecretary/ui/tools/SecretaryToolsActivity.java
// =========================================================
package com.lsam.pocketsecretary.ui.tools;

import android.content.Intent;
import android.os.Bundle;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.ManualNotificationActivity;
import com.lsam.pocketsecretary.ui.history.HistoryActivity;
import com.lsam.pocketsecretary.ui.hub.AllFeaturesActivity;
import com.lsam.pocketsecretary.ui.speech.SpeechToolActivity;

public class SecretaryToolsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBaseContent(R.layout.activity_secretary_tools);

        bindActions();
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_feature_hub_title);
    }

    private void bindActions() {

        findViewById(R.id.btnManualNotify).setOnClickListener(v ->
                startActivity(new Intent(this, ManualNotificationActivity.class))
        );

        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );

        findViewById(R.id.btnSpeechTool).setOnClickListener(v ->
                startActivity(new Intent(this, SpeechToolActivity.class))
        );

        // 新規：全機能ハブ
        findViewById(R.id.btnOpenAllFeatures).setOnClickListener(v ->
                startActivity(new Intent(this, AllFeaturesActivity.class))
        );
    }
}
