package com.lsam.pocketsecretary.ui.settings;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.core.secretary.Secretary;
import com.lsam.pocketsecretary.core.secretary.SecretaryCatalog;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Switch swNotify;
    private Spinner spSecretary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView title = findViewById(R.id.txtSettingsTitle);
        title.setText("Settings");

        swNotify = findViewById(R.id.swNotifyEnabled);
        spSecretary = findViewById(R.id.spDefaultSecretary);

        swNotify.setChecked(Prefs.isNotifyEnabled(this));
        swNotify.setOnCheckedChangeListener((buttonView, isChecked) ->
                Prefs.setNotifyEnabled(this, isChecked));

        List<Secretary> all = SecretaryCatalog.all();
        List<String> labels = new ArrayList<>();
        for (Secretary s : all) labels.add(s.name + " (" + s.tone + ")");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                labels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSecretary.setAdapter(adapter);

        String cur = Prefs.getDefaultSecretary(this);
        int idx = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).id.equals(cur)) { idx = i; break; }
        }
        spSecretary.setSelection(idx);

        spSecretary.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                Prefs.setDefaultSecretary(SettingsActivity.this, all.get(position).id);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }
}
