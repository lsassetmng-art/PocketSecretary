package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;
import android.os.Bundle;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;
import android.widget.Button;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;

import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;
import androidx.recyclerview.widget.RecyclerView;
import com.lsam.pocketsecretary.core.guard.PermissionGuard;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.stats.LocalStats;
import com.lsam.pocketsecretary.core.voice.VoiceManager;
import com.lsam.pocketsecretary.ui.anim.SecretaryAnimator;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;
import java.util.*;

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

        // Phase8: 蛻晏屓繧ｪ繝ｳ繝懊・繝会ｼ域怙蟆擾ｼ・
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
        items.add(new SecretaryItem("hiyori", "縺ｲ繧医ｊ", "豈取律繧偵◎縺｣縺ｨ謾ｯ縺医∪縺・));
        items.add(new SecretaryItem("aoi", "縺ゅ♀縺・, "莠亥ｮ壹ｒ髱吶°縺ｫ謨ｴ逅・＠縺ｾ縺・));
        items.add(new SecretaryItem("ren", "繧後ｓ", "蠢・ｦ√↑縺薙→縺縺台ｼ昴∴縺ｾ縺・));

        recyclerView.setAdapter(new SecretaryListAdapter(items, item -> {
            Prefs.setDefaultSecretary(this, item.id);
            android.widget.Toast.makeText(this, item.name + "縺悟ｼ輔″邯吶℃縺ｾ縺・, android.widget.Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SecretaryChatActivity.class);
            i.putExtra(SecretaryChatActivity.EXTRA_SECRETARY_ID, item.id);
            startActivity(i);
        }));
    }

    @Override
protected void onResume() {
    super.onResume();

    try {
        android.widget.TextView tv =
            findViewById(R.id.txtNextEvent);
        if (tv != null) {
            com.lsam.pocketsecretary.core.notification.NextEventPicker.Picked p =
                com.lsam.pocketsecretary.core.notification.NextEventPicker.pick(this);
            tv.setText(p == null ? "次の予定：なし" : "次の予定：" + p.title);
        }
    } catch (Exception ignore) {}
}
        com.lsam.pocketsecretary.core.notification.WeeklySummaryScheduler.schedule(this);
        android.widget.TextView tv=findViewById(R.id.txtNextEvent);
        com.lsam.pocketsecretary.core.notification.NextEventPicker.Picked p=com.lsam.pocketsecretary.core.notification.NextEventPicker.pick(this);
        tv.setText(p==null?"谺｡縺ｮ莠亥ｮ夲ｼ壹↑縺・:"谺｡縺ｮ莠亥ｮ夲ｼ・+p.title);
        com.lsam.pocketsecretary.core.notification.AutoNotifyScheduler.rescheduleNext(this);
        super.onResume();
        VoiceManager.speak(this, "谺｡縺ｮ莠亥ｮ壹ｒ遒ｺ隱阪＠縺ｾ縺・);
        SecretaryAnimator.breathe(findViewById(android.R.id.content));
        android.widget.TextView st=findViewById(R.id.txtStats);
        if (st!=null) st.setText(com.lsam.pocketsecretary.core.stats.LocalStats.summary(this));
        android.widget.TextView ft=findViewById(R.id.txtFreeTime);
        if (ft!=null){ ft.setText("莉頑律縺ｮ遨ｺ縺肴凾髢薙・險育ｮ嶺ｸｭ窶ｦ"); }
        android.widget.TextView card = findViewById(R.id.cardPermission);
        if (card != null) {
            boolean okN = PermissionGuard.hasNotify(this);
            boolean okC = PermissionGuard.hasCalendar(this);
            card.setVisibility((okN && okC) ? android.view.View.GONE : android.view.View.VISIBLE);
        }
        com.lsam.pocketsecretary.core.notification.WeeklySummaryScheduler.schedule(this);
        android.widget.TextView tv=findViewById(R.id.txtNextEvent);
        com.lsam.pocketsecretary.core.notification.NextEventPicker.Picked p=com.lsam.pocketsecretary.core.notification.NextEventPicker.pick(this);
        tv.setText(p==null?"谺｡縺ｮ莠亥ｮ夲ｼ壹↑縺・:"谺｡縺ｮ莠亥ｮ夲ｼ・+p.title);
        // Phase6.5: Notify OFF 繧定ｦ冶ｦ壼喧
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
