package com.lsam.pocketsecretary.ui.dashboard;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.content.Intent;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.core.persona.PersonaLoader;
import com.lsam.pocketsecretary.core.persona.PersonaEmotionApplier;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;
import com.lsam.pocketsecretary.ui.persona.PersonaSelectBottomSheet;
import com.lsam.pocketsecretary.worker.MorningBriefingWorker;
import com.lsam.pocketsecretary.worker.UpcomingEventWorker;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends BaseActivity {

    private ImageView bg;
    private TextView personaName;
    private TextView count;
    private TextView next;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);

        // ✅ BaseActivity方式
        setBaseContent(R.layout.activity_dashboard_persona);

        bg = findViewById(R.id.psPersonaBackground);
        personaName = findViewById(R.id.txtPersonaName);
        count = findViewById(R.id.txtTodayCount);
        next  = findViewById(R.id.txtNextEvent);

        // ===== 読み上げ =====
        findViewById(R.id.btnSpeechTool).setOnClickListener(v ->
                startActivity(new Intent(this,
                        com.lsam.pocketsecretary.ui.speech.SpeechToolActivity.class))
        );

        // ===== 履歴 =====
        if (findViewById(R.id.btnHistory) != null) {
            findViewById(R.id.btnHistory).setOnClickListener(v ->
                    startActivity(new Intent(this,
                            com.lsam.pocketsecretary.ui.history.HistoryActivity.class))
            );
        }

        // ===== 手動通知 =====
        if (findViewById(R.id.btnNotification) != null) {
            findViewById(R.id.btnNotification).setOnClickListener(v ->
                    startActivity(new Intent(this,
                            com.lsam.pocketsecretary.ManualNotificationActivity.class))
            );
        }

        // ===== Persona切替 =====
        personaName.setOnClickListener(v -> {

            PersonaSelectBottomSheet sheet =
                    new PersonaSelectBottomSheet(personaId -> {

                        CurrentPersonaStore.set(this, personaId);
                        applyCurrentPersona();
                    });

            sheet.show(getSupportFragmentManager(), "persona_select");
        });

        renderTodayInfo();

        // ===== 朝通知 =====
        MorningBriefingWorker.ensureScheduled(this);

        // ===== 予定前チェック =====
        registerUpcomingChecker();
    }

    @Override
    protected String getHeaderTitle() {
        return "Dashboard";
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCurrentPersona();
    }

    // =============================
    // 今日情報表示
    // =============================
    private void renderTodayInfo() {

        int c = TodayEngine.todayCount(this);
        String n = TodayEngine.nextTitle(this);

        count.setText(getString(R.string.dashboard_today_count, c));
        next.setText(getString(R.string.dashboard_next_event, n));

        boolean tight = TodayEngine.isTight(this);

        EmotionStateStore store = EmotionStateStore.getInstance();

        if(tight){
            count.setTextColor(Color.RED);
            store.set(EmotionStateStore.Emotion.ALERT);
        } else {
            count.setTextColor(Color.BLACK);
            store.set(EmotionStateStore.Emotion.CALM);
        }
    }

    // =============================
    // Persona反映
    // =============================
    private void applyCurrentPersona() {

        String personaId = CurrentPersonaStore.get(this);

        Bitmap bmp = PersonaLoader.loadPersonaImage(this, personaId);
        if (bmp != null) {
            bg.setImageBitmap(bmp);
        }

        PersonaEmotionApplier.applyBaseEmotion(this, personaId);

        Map<String, String> personaMap =
                PersonaYamlLoader.load(this, personaId);

        if (personaMap != null) {
            String displayName = personaMap.get("display_name");
            if (displayName != null) {
                personaName.setText(displayName);
            }
        }
    }

    // =============================
    // 予定前通知の定期チェック
    // =============================
    private void registerUpcomingChecker() {

        PeriodicWorkRequest req =
                new PeriodicWorkRequest.Builder(
                        UpcomingEventWorker.class,
                        15,
                        TimeUnit.MINUTES
                ).build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "upcoming_checker",
                        ExistingPeriodicWorkPolicy.KEEP,
                        req
                );
    }
}
