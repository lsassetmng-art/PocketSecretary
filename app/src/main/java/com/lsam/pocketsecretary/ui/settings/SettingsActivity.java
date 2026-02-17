package com.lsam.pocketsecretary.ui.settings;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.settings.NotificationSettingsStore;
import com.lsam.pocketsecretary.worker.MorningBriefingWorker;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends BaseActivity {

    private Switch swMorning;
    private TextView txtMorningTime;
    private EditText edtBefore;
    private Button btnSave;

    private int pickedHour = 8;
    private int pickedMinute = 0;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_settings);

        swMorning = findViewById(R.id.swMorningEnabled);
        txtMorningTime = findViewById(R.id.txtMorningTime);
        edtBefore = findViewById(R.id.edtBeforeMinutes);
        btnSave = findViewById(R.id.btnSaveSettings);

        Button btnPick = findViewById(R.id.btnPickMorningTime);

        loadSettings();

        if (btnPick != null) {
            btnPick.setOnClickListener(v -> {

                Calendar now = Calendar.getInstance();

                int h = pickedHour;
                int m = pickedMinute;

                if (h < 0 || h > 23) h = now.get(Calendar.HOUR_OF_DAY);
                if (m < 0 || m > 59) m = now.get(Calendar.MINUTE);

                new TimePickerDialog(
                        this,
                        (view, hour, minute) -> {
                            pickedHour = hour;
                            pickedMinute = minute;
                            txtMorningTime.setText(formatTime(hour, minute));
                        },
                        h,
                        m,
                        true
                ).show();
            });
        }

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                saveSettings();
                MorningBriefingWorker.ensureScheduled(this);

                Toast.makeText(
                        this,
                        getString(R.string.settings_saved),
                        Toast.LENGTH_SHORT
                ).show();
            });
        }
    }

    private void loadSettings() {

        boolean enabled =
                NotificationSettingsStore.isMorningEnabled(this);

        int hour =
                NotificationSettingsStore.getMorningHour(this);

        int minute =
                NotificationSettingsStore.getMorningMinute(this);

        int before =
                NotificationSettingsStore.getReminderBeforeMinutes(this);

        swMorning.setChecked(enabled);

        pickedHour = hour;
        pickedMinute = minute;

        txtMorningTime.setText(formatTime(hour, minute));
        edtBefore.setText(String.valueOf(before));
    }

    private void saveSettings() {

        int before = 30;

        String s = edtBefore.getText() != null ?
                edtBefore.getText().toString().trim() : "";

        if (!TextUtils.isEmpty(s)) {
            try {
                before = Integer.parseInt(s);
            } catch (Exception ignored) {}
        }

        NotificationSettingsStore.setMorningEnabled(
                this,
                swMorning.isChecked()
        );

        NotificationSettingsStore.setMorningTime(
                this,
                pickedHour,
                pickedMinute
        );

        NotificationSettingsStore.setReminderBeforeMinutes(
                this,
                before
        );
    }

    private static String formatTime(int hour, int minute) {
        return String.format(Locale.US, "%02d:%02d", hour, minute);
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
