package com.lsam.pocketsecretary.ui.settings;

import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);

        CheckBox c30 = findViewById(R.id.chk30);
        CheckBox c10 = findViewById(R.id.chk10);
        CheckBox c5  = findViewById(R.id.chk5);

        c30.setChecked(Prefs.getBool(this,"n30",true));
        c10.setChecked(Prefs.getBool(this,"n10",true));
        c5 .setChecked(Prefs.getBool(this,"n5", true));

        c30.setOnCheckedChangeListener((v,bv)->Prefs.putBool(this,"n30",bv));
        c10.setOnCheckedChangeListener((v,bv)->Prefs.putBool(this,"n10",bv));
        c5 .setOnCheckedChangeListener((v,bv)->Prefs.putBool(this,"n5", bv));
    }
}
