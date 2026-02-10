package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import android.os.Bundle;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import android.widget.Button;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;

import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import androidx.recyclerview.widget.RecyclerView;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.ui.calendar.CalendarReadActivity;
import com.lsam.pocketsecretary.ui.notification.NotificationCenterActivity;
import com.lsam.pocketsecretary.ui.onboarding.OnboardingActivity;
import com.lsam.pocketsecretary.ui.settings.SettingsActivity;
import com.lsam.pocketsecretary.ui.event.AddEventActivity;

import java.util.ArrayList;
import java.util.List;

public class SecretaryListActivity extends AppCompatActivity {

    private Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Phase8: 初回オンボード（最小）
        if (!Prefs.isOnboarded(this)) {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

        setContentView(R.layout.activity_secretary_list);

        Button btnCalendar = findViewById(R.id.btnCalendar);
        btnNotify = findViewById(R.id.btnNotify);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnAddEvent = findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(v -> startActivity(new Intent(this, AddEventActivity.class)));

        btnCalendar.setOnClickListener(v -> startActivity(new Intent(this, CalendarReadActivity.class)));
        btnNotify.setOnClickListener(v -> startActivity(new Intent(this, NotificationCenterActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        RecyclerView recyclerView = findViewById(R.id.recyclerSecretary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SecretaryItem> items = new ArrayList<>();
        items.add(new SecretaryItem("hiyori", "ひより", "毎日をそっと支えます"));
        items.add(new SecretaryItem("aoi", "あおい", "予定を静かに整理します"));
        items.add(new SecretaryItem("ren", "れん", "必要なことだけ伝えます"));

        recyclerView.setAdapter(new SecretaryListAdapter(items, item -> {
            Prefs.setDefaultSecretary(this, item.id);
            android.widget.Toast.makeText(this, item.name + "が引き継ぎます", android.widget.Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SecretaryChatActivity.class);
            i.putExtra(SecretaryChatActivity.EXTRA_SECRETARY_ID, item.id);
            startActivity(i);
        }));
    }

    @Override
    protected void onResume() {
        android.widget.TextView card = findViewById(R.id.cardPermission);
        if (card != null) {
            boolean okN = PermissionGuard.hasNotify(this);
            boolean okC = PermissionGuard.hasCalendar(this);
            card.setVisibility((okN && okC) ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        com.lsam.pocketsecretary.core.notification.WeeklySummaryScheduler.schedule(this);
        android.widget.TextView tv=findViewById(R.id.txtNextEvent);
        com.lsam.pocketsecretary.core.notification.NextEventPicker.Picked p=com.lsam.pocketsecretary.core.notification.NextEventPicker.pick(this);
        tv.setText(p==null?"次の予定：なし":"次の予定："+p.title);
        com.lsam.pocketsecretary.core.notification.AutoNotifyScheduler.rescheduleNext(this);
        super.onResume();
        android.widget.TextView card = findViewById(R.id.cardPermission);
        if (card != null) {
            boolean okN = PermissionGuard.hasNotify(this);
            boolean okC = PermissionGuard.hasCalendar(this);
            card.setVisibility((okN && okC) ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        com.lsam.pocketsecretary.core.notification.WeeklySummaryScheduler.schedule(this);
        android.widget.TextView tv=findViewById(R.id.txtNextEvent);
        com.lsam.pocketsecretary.core.notification.NextEventPicker.Picked p=com.lsam.pocketsecretary.core.notification.NextEventPicker.pick(this);
        tv.setText(p==null?"次の予定：なし":"次の予定："+p.title);
        // Phase6.5: Notify OFF を視覚化
        if (btnNotify != null) {
            boolean enabled = Prefs.isNotifyEnabled(this);
            btnNotify.setEnabled(enabled);
            btnNotify.setAlpha(enabled ? 1.0f : 0.45f);
        }
    }

    public static class SecretaryItem {
        public final String id;
        public final String name;
        public final String description;

        public SecretaryItem(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }
}
