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

import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;

// ✅ Phase J metrics
import com.lsam.pocketsecretary.core.metrics.UsageMetrics;

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

        setBaseContent(R.layout.activity_dashboard_persona);

        // ✅ Phase J: launch count
        new UsageMetrics(this).recordLaunch();

        bg = findViewById(R.id.psPersonaBackground);
        personaName = findViewById(R.id.txtPersonaName);
        count = findViewById(R.id.txtTodayCount);
        next  = findViewById(R.id.txtNextEvent);

        findViewById(R.id.btnSpeechTool).setOnClickListener(v ->
                startActivity(new Intent(this,
                        com.lsam.pocketsecretary.ui.speech.SpeechToolActivity.class))
        );

        if (findViewById(R.id.btnHistory) != null) {
            findViewById(R.id.btnHistory).setOnClickListener(v ->
                    startActivity(new Intent(this,
                            com.lsam.pocketsecretary.ui.history.HistoryActivity.class))
            );
        }

        if (findViewById(R.id.btnNotification) != null) {
            findViewById(R.id.btnNotification).setOnClickListener(v -> {

                // ✅ Phase J: notification shown metric
                new UsageMetrics(this).recordNotificationShown();

                startActivity(new Intent(this,
                        com.lsam.pocketsecretary.ManualNotificationActivity.class));
            });
        }

        personaName.setOnClickListener(v -> {

            PersonaSelectBottomSheet sheet =
                    new PersonaSelectBottomSheet(personaId -> {

                        CurrentPersonaStore.set(this, personaId);

                        // ✅ Phase J: persona change metric
                        new UsageMetrics(this).recordPersonaChange();

                        applyCurrentPersona();
                        renderTodayInfo();
                    });

            sheet.show(getSupportFragmentManager(), "persona_select");
        });

        renderTodayInfo();

        MorningBriefingWorker.ensureScheduled(this);

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
        renderTodayInfo();
    }

    private void renderTodayInfo() {

        int c = TodayEngine.todayCount(this);
        String n = TodayEngine.nextTitle(this);
        if (n == null) n = "";

        boolean tight = TodayEngine.isTight(this);

        EmotionStateStore store = EmotionStateStore.getInstance();
        EmotionState emotion;

        if (tight) {
            count.setTextColor(Color.RED);
            store.set(EmotionStateStore.Emotion.ALERT);
            emotion = EmotionState.ALERT;
        } else {
            count.setTextColor(Color.BLACK);
            store.set(EmotionStateStore.Emotion.CALM);
            emotion = EmotionState.CALM;
        }

        String personaId = CurrentPersonaStore.get(this);

        count.setText(getString(R.string.dashboard_today_count, c));

        String nextLine = PersonaEngine.generate(
                this,
                personaId,
                emotion,
                PersonaChannel.DASHBOARD,
                n,
                null,
                c
        ).text;

        next.setText(nextLine);
    }

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
