package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.ui.calendar.CalendarReadActivity;
import com.lsam.pocketsecretary.ui.notification.NotificationCenterActivity;
import com.lsam.pocketsecretary.ui.settings.SettingsActivity;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        // Phase8: 蛻晏屓繧ｪ繝ｳ繝懊・繝会ｼ域怙蟆擾ｼ・
        if (!Prefs.isOnboarded(this)) {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

=======
>>>>>>> 0f68ece (fix: termux-safe minimal secretary + notification receiver stub)
        setContentView(R.layout.activity_secretary_list);

        Button btnCalendar = findViewById(R.id.btnCalendar);
        Button btnNotify   = findViewById(R.id.btnNotify);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnCalendar.setOnClickListener(v ->
                startActivity(new Intent(this, CalendarReadActivity.class)));

        btnNotify.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationCenterActivity.class)));

<<<<<<< HEAD
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
=======
        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        TextView tv = findViewById(R.id.txtNextEvent);
        tv.setText("Next event: none");
>>>>>>> 0f68ece (fix: termux-safe minimal secretary + notification receiver stub)
    }
}
