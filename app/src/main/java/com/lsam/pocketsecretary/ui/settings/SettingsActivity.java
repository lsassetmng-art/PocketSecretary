package com.lsam.pocketsecretary.ui.settings;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;

import java.util.Calendar;

public class SettingsActivity extends BaseActivity {

    private static final String PREF_NAME = "ps_settings";
    private static final String KEY_MORNING_ENABLED = "morning_enabled";
    private static final String KEY_MORNING_TIME = "morning_time";
    private static final String KEY_BEFORE_MINUTES = "before_minutes";

    private Switch swMorning;
    private TextView txtMorningTime;
    private EditText edtBefore;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_settings);

        swMorning = findViewById(R.id.swMorningEnabled);
        txtMorningTime = findViewById(R.id.txtMorningTime);
        edtBefore = findViewById(R.id.edtBeforeMinutes);
        btnSave = findViewById(R.id.btnSaveSettings);

        Button btnPick = findViewById(R.id.btnPickMorningTime);

        // 🔥 既存設定を読み込み
        loadSettings();

        btnPick.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new TimePickerDialog(
                    this,
                    (view, hour, minute) ->
                            txtMorningTime.setText(
                                    String.format("%02d:%02d", hour, minute)
                            ),
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            ).show();
        });

        btnSave.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(
                    this,
                    getString(R.string.settings_saved),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // =========================================================
    // 🔵 読み込み
    // =========================================================
    private void loadSettings() {

        SharedPreferences pref =
                getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        swMorning.setChecked(
                pref.getBoolean(KEY_MORNING_ENABLED, false)
        );

        txtMorningTime.setText(
                pref.getString(KEY_MORNING_TIME, "07:30")
        );

        edtBefore.setText(
                String.valueOf(
                        pref.getInt(KEY_BEFORE_MINUTES, 10)
                )
        );
    }

    // =========================================================
    // 🔵 保存
    // =========================================================
    private void saveSettings() {

        SharedPreferences pref =
                getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        int before = 10;
        try {
            before = Integer.parseInt(
                    edtBefore.getText().toString()
            );
        } catch (Exception ignored) {}

        pref.edit()
                .putBoolean(KEY_MORNING_ENABLED, swMorning.isChecked())
                .putString(KEY_MORNING_TIME, txtMorningTime.getText().toString())
                .putInt(KEY_BEFORE_MINUTES, before)
                .apply();
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.title_settings);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
