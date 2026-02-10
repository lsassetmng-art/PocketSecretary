package com.lsam.centergravitypuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TitleActivity extends AppCompatActivity {
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        settings = new AppSettings(this);

        TextView ver = findViewById(R.id.txtVersion);
        ver.setText("CenterGravityPuzzle / Phase5 (pre-release)");

        Switch swSe = findViewById(R.id.swSe);
        Switch swVib = findViewById(R.id.swVib);

        swSe.setChecked(settings.isSeEnabled());
        swVib.setChecked(settings.isVibrationEnabled());

        swSe.setOnCheckedChangeListener((b, v) -> settings.setSeEnabled(v));
        swVib.setOnCheckedChangeListener((b, v) -> settings.setVibrationEnabled(v));

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class));
        });

        findViewById(R.id.btnHow).setOnClickListener(v -> {
            startActivity(new Intent(this, HowToPlayActivity.class));
        });
    }
}
