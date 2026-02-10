package com.lsam.pocketsecretary.ui.settings;

import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        findViewById(R.id.btnWeekly).setOnClickListener(v->startActivity(new android.content.Intent(this, com.lsam.pocketsecretary.ui.week.WeeklyReadActivity.class)));
        super.onCreate(b);
        findViewById(R.id.btnWeekly).setOnClickListener(v->startActivity(new android.content.Intent(this, com.lsam.pocketsecretary.ui.week.WeeklyReadActivity.class)));
        setContentView(R.layout.activity_settings);

        CheckBox c30 = findViewById(R.id.chk30);
        CheckBox c10 = findViewById(R.id.chk10);
        CheckBox c5  = findViewById(R.id.chk5);


    }
}
