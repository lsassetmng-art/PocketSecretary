package com.lsam.pocketsecretary.ui.dashboard;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.content.Intent;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.core.persona.PersonaLoader;
import com.lsam.pocketsecretary.core.persona.PersonaEmotionApplier;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;
import com.lsam.pocketsecretary.ui.persona.PersonaSelectBottomSheet;
import com.lsam.pocketsecretary.ui.tools.SecretaryToolsActivity;
import com.lsam.pocketsecretary.worker.MorningBriefingWorker;
import com.lsam.pocketsecretary.worker.UpcomingEventWorker;
import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;
import com.lsam.pocketsecretary.core.metrics.UsageMetrics;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends BaseActivity {

    private ImageView personaCardImage;
    private TextView personaName;
    private TextView nextLine;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);

        setBaseContent(R.layout.activity_dashboard_persona);

        personaName = findViewById(R.id.txtPersonaName);
        nextLine = findViewById(R.id.txtNextLine);
        personaCardImage = findViewById(R.id.imgPersonaCard);

        findViewById(R.id.btnOpenTools).setOnClickListener(v ->
                startActivity(new Intent(this, SecretaryToolsActivity.class))
        );

        personaName.setOnClickListener(v -> {
            PersonaSelectBottomSheet sheet =
                    new PersonaSelectBottomSheet(personaId -> {
                        CurrentPersonaStore.set(this, personaId);
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
        return "ダッシュボード";
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCurrentPersona();
        renderTodayInfo();
    }

    private void renderTodayInfo() {

        int todayCount = TodayEngine.todayCount(this);
        String nextTitle = TodayEngine.nextTitle(this);
        if (nextTitle == null) nextTitle = "";

        String personaId = CurrentPersonaStore.get(this);

        String generated = PersonaEngine.generate(
                this,
                personaId,
                EmotionState.CALM,
                PersonaChannel.DASHBOARD,
                nextTitle,
                null,
                todayCount
        ).text;

        // ===== Phase N 成長演出（数値非表示） =====
        int streak = new UsageMetrics(this).getStreakDays();

        if (streak >= 7) {
            generated += "\n" + getString(R.string.ps_growth_stage_2);
        } else if (streak >= 3) {
            generated += "\n" + getString(R.string.ps_growth_stage_1);
        }

        nextLine.setText(generated);
    }

    private void applyCurrentPersona() {

        String personaId = CurrentPersonaStore.get(this);

        Bitmap bmp = PersonaLoader.loadPersonaImage(this, personaId);
        if (bmp != null && personaCardImage != null) {
            personaCardImage.setImageBitmap(bmp);
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
