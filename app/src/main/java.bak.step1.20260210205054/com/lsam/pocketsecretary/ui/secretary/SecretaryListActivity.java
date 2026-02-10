package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.model.SecretaryItem;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;

import java.util.ArrayList;
import java.util.List;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_secretary_list);

        RecyclerView rv = findViewById(R.id.recyclerSecretary);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<SecretaryItem> items = new ArrayList<>();
        items.add(new SecretaryItem(
            "hiyori",
            getString(R.string.secretary_hiyori_name),
            getString(R.string.secretary_hiyori_desc)
        ));
        items.add(new SecretaryItem(
            "aoi",
            getString(R.string.secretary_aoi_name),
            getString(R.string.secretary_aoi_desc)
        ));
        items.add(new SecretaryItem(
            "ren",
            getString(R.string.secretary_ren_name),
            getString(R.string.secretary_ren_desc)
        ));

        rv.setAdapter(new SecretaryListAdapter(items, item -> {
            Prefs.setDefaultSecretary(this, item.id);
            startActivity(new Intent(this, SecretaryChatActivity.class));
        }));

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v ->
            startActivity(new Intent(this, com.lsam.pocketsecretary.ui.settings.SettingsActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        VoiceManager.speak(this, getString(R.string.voice_check_next));
        SecretaryAnimator.breathe(findViewById(android.R.id.content));

        TextView ft = findViewById(R.id.txtFreeTime);
        if (ft != null) ft.setText(getString(R.string.free_time_calc));
    }
}
