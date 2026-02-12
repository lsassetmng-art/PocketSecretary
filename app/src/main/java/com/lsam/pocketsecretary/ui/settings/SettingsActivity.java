package com.lsam.pocketsecretary.ui.settings;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private Switch swMorning;
    private TextView txtMorningTime;
    private EditText edtBefore;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);

        swMorning = findViewById(R.id.swMorningEnabled);
        txtMorningTime = findViewById(R.id.txtMorningTime);
        edtBefore = findViewById(R.id.edtBeforeMinutes);
        btnSave = findViewById(R.id.btnSaveSettings);

        Button btnPick = findViewById(R.id.btnPickMorningTime);

        btnPick.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new TimePickerDialog(
                    this,
                    (view, hour, minute) ->
                            txtMorningTime.setText(String.format("%02d:%02d", hour, minute)),
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            ).show();
        });

        btnSave.setOnClickListener(v ->
                Toast.makeText(this,
                        getString(R.string.settings_saved),
                        Toast.LENGTH_SHORT).show()
        );
    }
}
