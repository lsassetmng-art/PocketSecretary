package com.lsam.pocketsecretary;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lsam.pocketsecretary.core.settings.NotificationSettingsStore;
import com.lsam.pocketsecretary.worker.MorningBriefingWorker;

public class SettingsActivity extends BaseActivity {

    private Switch swMorning;
    private TextView txtMorningTime;
    private Button btnPickTime;

    private EditText edtBeforeMin;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ BaseActivity方式
        setBaseContent(R.layout.activity_settings);

        swMorning = findViewById(R.id.swMorningEnabled);
        txtMorningTime = findViewById(R.id.txtMorningTime);
        btnPickTime = findViewById(R.id.btnPickMorningTime);

        edtBeforeMin = findViewById(R.id.edtBeforeMinutes);
        btnSave = findViewById(R.id.btnSaveSettings);

        load();

        // =========================
        // 朝通知ON/OFF
        // =========================
        swMorning.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationSettingsStore.setMorningEnabled(this, isChecked);

            if (isChecked) {
                MorningBriefingWorker.ensureScheduled(this);
            }
        });

        // =========================
        // 時刻選択
        // =========================
        btnPickTime.setOnClickListener(v -> {

            int h = NotificationSettingsStore.getMorningHour(this);
            int m = NotificationSettingsStore.getMorningMinute(this);

            TimePickerDialog dlg = new TimePickerDialog(
                    this,
                    (TimePicker view, int hourOfDay, int minute) -> {

                        NotificationSettingsStore
                                .setMorningTime(this, hourOfDay, minute);

                        load();

                        if (NotificationSettingsStore
                                .isMorningEnabled(this)) {

                            MorningBriefingWorker.ensureScheduled(this);
                        }
                    },
                    h,
                    m,
                    true
            );

            dlg.show();
        });

        // =========================
        // 予定前通知（分）
        // =========================
        btnSave.setOnClickListener(v -> {

            String s = edtBeforeMin.getText().toString().trim();
            int before = 30;

            try {
                before = Integer.parseInt(s);
            } catch (Exception ignored) {}

            NotificationSettingsStore
                    .setReminderBeforeMinutes(this, before);

            Toast.makeText(
                    this,
                    getString(R.string.settings_saved),
                    Toast.LENGTH_SHORT
            ).show();

            load();
        });
    }

    @Override
    protected String getHeaderTitle() {
        return "Settings";
    }

    // =============================
    // 現在値反映
    // =============================
    private void load() {

        boolean morningEnabled =
                NotificationSettingsStore.isMorningEnabled(this);

        int h =
                NotificationSettingsStore.getMorningHour(this);

        int m =
                NotificationSettingsStore.getMorningMinute(this);

        swMorning.setChecked(morningEnabled);

        txtMorningTime.setText(
                String.format("%02d:%02d", h, m)
        );

        int before =
                NotificationSettingsStore
                        .getReminderBeforeMinutes(this);

        edtBeforeMin.setText(String.valueOf(before));
    }
}
