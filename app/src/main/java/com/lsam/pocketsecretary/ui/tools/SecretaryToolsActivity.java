package com.lsam.pocketsecretary.ui.tools;

import android.content.Intent;
import android.os.Bundle;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.ManualNotificationActivity;
import com.lsam.pocketsecretary.ui.history.HistoryActivity;
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
        return "秘書ツール";
    }

    // =========================
    // Actions
    // =========================

    private void bindActions() {

        // 通知
        findViewById(R.id.btnManualNotify).setOnClickListener(v ->
                startActivity(new Intent(this, ManualNotificationActivity.class))
        );

        // 履歴
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );

        // 読み上げ
        findViewById(R.id.btnSpeechTool).setOnClickListener(v ->
                startActivity(new Intent(this, SpeechToolActivity.class))
        );
    }
}
