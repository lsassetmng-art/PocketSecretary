package com.lsam.pocketsecretary.ui.dashboard;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.lsam.pocketsecretary.core.persona.PersonaEmotionApplier;
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
        personaName = findViewById(R.id.txtPersonaName);
        nextLine = findViewById(R.id.txtNextLine);
        personaCardImage = findViewById(R.id.imgPersonaCard);

        applyBackground();
        applyCurrentPersona();
        renderTodayInfo();

        MorningBriefingWorker.ensureScheduled(this);
        registerUpcomingChecker();
        startBreathingAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyBackground();
        applyCurrentPersona();
        renderTodayInfo();
        startBreathingAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBreathingAnimation();
    }

    // =========================================================
    // Background
    // =========================================================
    private void applyBackground() {

        if (backgroundImage == null) return;

        String backgroundPackId = BackgroundStore.get(this);
        if (backgroundPackId == null || backgroundPackId.isEmpty()) {
            backgroundPackId = "desk_set_001";
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String type;
        if (hour <= 10) type = "morning";
        else if (hour <= 17) type = "day";
        else type = "night";

        Bitmap bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, type));
        if (bmp == null) bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "day"));
        if (bmp == null) bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "morning"));
        if (bmp == null) bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "night"));

        if (bmp != null) backgroundImage.setImageBitmap(bmp);
    }

    // =========================================================
    // Today + PersonaOS (JWT必須版)
    // =========================================================
    private void renderTodayInfo() {

        if (nextLine == null) return;

        int todayCount = TodayEngine.todayCount(this);
        String nextTitle = TodayEngine.nextTitle(this);
        if (nextTitle == null) nextTitle = "";

        String personaId = CurrentPersonaStore.get(this);

        PersonaResponse response = PersonaEngine.generate(
                this,
                personaId,
                EmotionState.CALM,
                PersonaChannel.DASHBOARD,
                nextTitle,
                null,
                todayCount
        );

        nextLine.setText(response.text);

        // 🔐 PersonaOS 呼び出し（JWT必須）
        try {
            PersonaOsGateway.onDashboardOpened(
                    this,
                    personaId
            );
        } catch (Throwable ignored) {}

        PersonaToneTag tone =
                response.toneTag != null
                        ? response.toneTag
                        : PersonaToneTag.CALM;

        EmotionAnimator.apply(personaCardImage, tone);
    }

    // =========================================================
    // Persona base + visual-compose
    // =========================================================
    private void applyCurrentPersona() {

        if (personaCardImage == null) return;

        String personaId = CurrentPersonaStore.get(this);
        if (personaId == null || personaId.isEmpty()) personaId = "alpha";

        PersonaMetaLoader.PersonaMeta meta = PersonaMetaLoader.load(this, personaId);
        if (meta == null) return;

        String visualPackId = meta.requiredVisualPackId;
        if (visualPackId == null || visualPackId.isEmpty()) visualPackId = "michelle_default";

        String baseSkinId = SkinStore.get(this, personaId, meta.defaultSkin);
        if (baseSkinId == null || baseSkinId.isEmpty()) baseSkinId = "default";

        Bitmap bmp = AssetRepository.loadBitmap(
                this,
                AssetRepository.visualSkinImage(visualPackId, baseSkinId, "character_base.png")
        );

        if (bmp != null) {
            personaCardImage.setImageBitmap(bmp);
        }

        PersonaEmotionApplier.applyBaseEmotion(this, personaId);

        if (personaName != null) {
            personaName.setText(
                    meta.displayName != null && !meta.displayName.isEmpty()
                            ? meta.displayName
                            : personaId
            );
        }

        VisualComposeRepository.applyIfConfigured(this, personaId, personaCardImage);
    }

    private void startBreathingAnimation() {
        if (personaCardImage == null) return;

        stopBreathingAnimation();

        breathingAnimator = ValueAnimator.ofFloat(1.0f, 1.015f);
        breathingAnimator.setDuration(2200);
        breathingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        breathingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        breathingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        breathingAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            personaCardImage.setScaleY(scale);
        });

        breathingAnimator.start();
    }

    private void stopBreathingAnimation() {
        if (breathingAnimator != null) {
            breathingAnimator.cancel();
            breathingAnimator = null;
        }

        if (personaCardImage != null) {
            personaCardImage.setScaleY(1.0f);
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
    protected String getHeaderTitle() {
        return getString(R.string.app_name);
    }

}
