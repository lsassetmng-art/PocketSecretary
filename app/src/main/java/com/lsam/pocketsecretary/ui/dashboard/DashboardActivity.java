// =========================================================
// app/src/main/java/com/lsam/pocketsecretary/ui/dashboard/DashboardActivity.java
// ※ あなたの貼った版のまま、btnScheduleだけ EventListActivity に向ける
// =========================================================
package com.lsam.pocketsecretary.ui.dashboard;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.assets.AssetRepository;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaMetaLoader;
import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.edge.PersonaOsGateway;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.PersonaResponse;
import com.lsam.pocketsecretary.core.personaos.model.PersonaToneTag;
import com.lsam.pocketsecretary.core.personaos.visual.VisualComposeRepository;
import com.lsam.pocketsecretary.core.settings.BackgroundStore;
import com.lsam.pocketsecretary.core.settings.SkinStore;
import com.lsam.pocketsecretary.ui.archive.ArchiveActivity;
import com.lsam.pocketsecretary.ui.background.BackgroundSelectActivity;
import com.lsam.pocketsecretary.ui.event.EventListActivity;
import com.lsam.pocketsecretary.ui.persona.PersonaSelectActivity;
import com.lsam.pocketsecretary.ui.tools.SecretaryToolsActivity;
import com.lsam.pocketsecretary.worker.MorningBriefingWorker;
import com.lsam.pocketsecretary.worker.UpcomingEventWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends BaseActivity {

    private ImageView backgroundImage;
    private ImageView personaCardImage;
    private TextView personaName;
    private TextView nextLine;

    private ValueAnimator breathingAnimator;

    @Override
    protected void onCreate(@Nullable Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_dashboard_persona);

        backgroundImage = findViewById(R.id.imgDashboardBackground);
        personaCardImage = findViewById(R.id.imgPersonaCard);
        personaName = findViewById(R.id.txtPersonaName);
        nextLine = findViewById(R.id.txtNextLine);

        wireNavigation();

        applyBackground();
        applyCurrentPersona();
        renderTodayInfo();

        MorningBriefingWorker.ensureScheduled(this);
        registerUpcomingChecker();
        startBreathingAnimation();
    }

    private void wireNavigation() {

        if (backgroundImage != null) {
            backgroundImage.setOnClickListener(v ->
                    startActivity(new Intent(this, BackgroundSelectActivity.class))
            );
        }

        View.OnClickListener personaListener =
                v -> startActivity(new Intent(this, PersonaSelectActivity.class));

        if (personaCardImage != null) personaCardImage.setOnClickListener(personaListener);
        if (personaName != null) personaName.setOnClickListener(personaListener);

        View btnTools = findViewById(R.id.btnOpenTools);
        if (btnTools != null) {
            btnTools.setOnClickListener(v ->
                    startActivity(new Intent(this, SecretaryToolsActivity.class)));
        }

        View btnArchive = findViewById(R.id.btnArchive);
        if (btnArchive != null) {
            btnArchive.setOnClickListener(v ->
                    startActivity(new Intent(this, ArchiveActivity.class)));
        }

        // ✅ 予定確認ボタン -> EventListActivity
        View btnSchedule = findViewById(R.id.btnSchedule);
        if (btnSchedule != null) {
            btnSchedule.setOnClickListener(v ->
                    startActivity(new Intent(this, EventListActivity.class)));
        }
    }

    private void applyBackground() {
        if (backgroundImage == null) return;

        String packId = BackgroundStore.get(this);
        if (packId == null || packId.isEmpty()) packId = "desk_set_001";

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String type = hour <= 10 ? "morning" : hour <= 17 ? "day" : "night";

        Bitmap bmp = AssetRepository.loadBitmap(
                this,
                AssetRepository.backgroundImage(packId, type)
        );

        if (bmp != null) backgroundImage.setImageBitmap(bmp);
    }

    private void applyCurrentPersona() {

        if (personaCardImage == null) return;

        String personaId = CurrentPersonaStore.get(this);
        if (personaId == null || personaId.isEmpty()) personaId = "alpha";

        PersonaMetaLoader.PersonaMeta meta = PersonaMetaLoader.load(this, personaId);
        if (meta == null) return;

        String visualPackId = meta.requiredVisualPackId;
        if (visualPackId == null || visualPackId.isEmpty()) visualPackId = "michelle_default";

        String skinId = SkinStore.get(this, personaId, meta.defaultSkin);
        if (skinId == null || skinId.isEmpty()) skinId = "default";

        Bitmap bmp = AssetRepository.loadBitmap(
                this,
                AssetRepository.visualSkinImage(visualPackId, skinId, "character_base.png")
        );

        if (bmp != null) personaCardImage.setImageBitmap(bmp);
        if (personaName != null) personaName.setText(meta.displayName);

        VisualComposeRepository.applyIfConfigured(this, personaId, personaCardImage);
    }

    private void renderTodayInfo() {

        if (nextLine == null) return;

        String personaId = CurrentPersonaStore.get(this);
        if (personaId == null || personaId.isEmpty()) personaId = "alpha";

        int todayCount = TodayEngine.todayCount(this);
        String nextTitle = TodayEngine.nextTitle(this);

        PersonaResponse response =
                PersonaEngine.generate(
                        this,
                        personaId,
                        EmotionState.CALM,
                        PersonaChannel.DASHBOARD,
                        nextTitle,
                        null,
                        todayCount
                );

        nextLine.setText(response.text);

        try { PersonaOsGateway.onDashboardOpened(this, personaId); }
        catch (Throwable ignored) {}

        PersonaToneTag tone =
                response.toneTag != null ? response.toneTag : PersonaToneTag.CALM;

        EmotionAnimator.apply(personaCardImage, tone);
    }

    private void startBreathingAnimation() {

        stopBreathingAnimation();

        breathingAnimator = ValueAnimator.ofFloat(1.0f, 1.015f);
        breathingAnimator.setDuration(2200);
        breathingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        breathingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        breathingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        breathingAnimator.addUpdateListener(animation -> {
            if (personaCardImage != null) {
                personaCardImage.setScaleY((float) animation.getAnimatedValue());
            }
        });

        breathingAnimator.start();
    }

    private void stopBreathingAnimation() {
        if (breathingAnimator != null) {
            breathingAnimator.cancel();
            breathingAnimator = null;
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

    @Override
    protected void onResume() {
        super.onResume();
        applyBackground();
        applyCurrentPersona();
        renderTodayInfo();
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected boolean showSettingsButton() {
        return true;
    }
}
