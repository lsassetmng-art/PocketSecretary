package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.AutoNotifyScheduler;
import com.lsam.pocketsecretary.ui.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_list);

        View root = findViewById(R.id.root);
        // 演出（軽量・安全）：ふわっと表示
        if (root != null) {
            root.setAlpha(0f);
            root.animate().alpha(1f).setDuration(260).start();
        }

        TextView next = findViewById(R.id.txtNextEvent);
        if (next != null) next.setText(getString(R.string.label_next_event_none));

        ListView list = findViewById(R.id.list);

        List<String> items = new ArrayList<>();
        items.add(getString(R.string.secretary_hiyori) + "  - " + getString(R.string.desc_hiyori));
        items.add(getString(R.string.secretary_aoi)   + "  - " + getString(R.string.desc_aoi));
        items.add(getString(R.string.secretary_ren)   + "  - " + getString(R.string.desc_ren));

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(ad);

        list.setOnItemClickListener((p, v, pos, id) -> {
            String name = (pos==0)?getString(R.string.secretary_hiyori):(pos==1)?getString(R.string.secretary_aoi):getString(R.string.secretary_ren);
            Toast.makeText(this, getString(R.string.toast_takeover, name), Toast.LENGTH_SHORT).show();
        });

        Button btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        }

        Button btnNotifyTest = findViewById(R.id.btnNotifyTest);
        if (btnNotifyTest != null) {
            btnNotifyTest.setOnClickListener(v -> {
                AutoNotifyScheduler.scheduleDemo(this);
                Toast.makeText(this, "通知を予約しました（10分後）", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
