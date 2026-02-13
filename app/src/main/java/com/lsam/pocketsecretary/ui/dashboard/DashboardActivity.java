package com.lsam.pocketsecretary.ui.dashboard;

import android.animation.ValueAnimator;
import android.content.Intent;
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
import com.lsam.pocketsecretary.core.metrics.UsageMetrics;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaEmotionApplier;
import com.lsam.pocketsecretary.core.persona.PersonaMetaLoader;
import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.settings.BackgroundStore;
import com.lsam.pocketsecretary.core.settings.SkinStore;
import com.lsam.pocketsecretary.ui.archive.ArchiveActivity;
import com.lsam.pocketsecretary.ui.background.BackgroundPickerBottomSheet;
import com.lsam.pocketsecretary.ui.persona.PersonaSelectBottomSheet;
import com.lsam.pocketsecretary.ui.persona.SkinPickerBottomSheet;
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
        personaName = findViewById(R.id.txtPersonaName);
        nextLine = findViewById(R.id.txtNextLine);
        personaCardImage = findViewById(R.id.imgPersonaCard);

        // ===== ボタン安全化 =====

        if (findViewById(R.id.btnOpenTools) != null) {
            findViewById(R.id.btnOpenTools).setOnClickListener(v ->
                    startActivity(new Intent(this, SecretaryToolsActivity.class))
            );
        }

        if (findViewById(R.id.btnArchive) != null) {
            findViewById(R.id.btnArchive).setOnClickListener(v ->
                    startActivity(new Intent(this, ArchiveActivity.class))
            );
        }

        if (findViewById(R.id.btnSchedule) != null) {
            findViewById(R.id.btnSchedule).setOnClickListener(v -> {
                // TODO ScheduleActivity
            });
        }

        // ※現状 btnSettings は「背景ピッカー」扱い（仕様通りなら後で Settings に戻してOK）
        if (findViewById(R.id.btnSettings) != null) {
            findViewById(R.id.btnSettings).setOnClickListener(v ->
                    new BackgroundPickerBottomSheet(() -> {
                        applyBackground();
                    }).show(getSupportFragmentManager(), "bg_picker")
            );
        }

        // ===== Persona選択 =====
        if (personaName != null) {

            personaName.setOnClickListener(v -> {
                PersonaSelectBottomSheet sheet =
                        new PersonaSelectBottomSheet(personaId -> {
                            CurrentPersonaStore.set(this, personaId);
                            applyCurrentPersona();
                            renderTodayInfo();
                        });
                sheet.show(getSupportFragmentManager(), "persona_select");
            });

            // ★長押しでSkin切替（skin_id は VisualPack の skins/{skin_id} を指す）
            personaName.setOnLongClickListener(v -> {

                String personaId = CurrentPersonaStore.get(this);

                new SkinPickerBottomSheet(personaId, () -> {
                    applyCurrentPersona();
                }).show(getSupportFragmentManager(), "skin_picker");

                return true;
            });
        }

        applyBackground();
        applyCurrentPersona();
        renderTodayInfo();

        MorningBriefingWorker.ensureScheduled(this);
        registerUpcomingChecker();

        startBreathingAnimation();
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.header_dashboard);
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
    // 背景（Canonical v1.1: runtime/background/{pack_id}/(morning|day|night).png を読む）
    // =========================================================
    private void applyBackground() {

        if (backgroundImage == null) return;

        String backgroundPackId = BackgroundStore.get(this);
        if (backgroundPackId == null || backgroundPackId.isEmpty()) {
            backgroundPackId = "desk_set_001"; // 初期同梱前提（数値表示なし）
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String type;
        if (hour <= 10) type = "morning";
        else if (hour <= 17) type = "day";
        else type = "night";

        // まず時間帯 → 無ければ day/morning/night の順でフォールバック（安全化）
        Bitmap bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, type));
        if (bmp == null) {
            bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "day"));
        }
        if (bmp == null) {
            bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "morning"));
        }
        if (bmp == null) {
            bmp = AssetRepository.loadBitmap(this, AssetRepository.backgroundImage(backgroundPackId, "night"));
        }

        if (bmp != null) {
            backgroundImage.setImageBitmap(bmp);
        }
    }

    // =========================================================
    // 今日情報
    // =========================================================
    private void renderTodayInfo() {

        if (nextLine == null) return;

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

        int streak = new UsageMetrics(this).getStreakDays();
        if (streak >= 7) {
            generated += "\n" + getString(R.string.ps_growth_stage_2);
        } else if (streak >= 3) {
            generated += "\n" + getString(R.string.ps_growth_stage_1);
        }

        nextLine.setText(generated);
    }

    // =========================================================
    // Persona適用（Canonical v1.1: runtime/persona -> persona.yaml から required_visual_pack_id を引き、
    //                           runtime/visual/{visual_pack_id}/skins/{skin_id}/character_base.png を読む）
    // =========================================================
    private void applyCurrentPersona() {

        if (personaCardImage == null) return;

        String personaId = CurrentPersonaStore.get(this);
        if (personaId == null || personaId.isEmpty()) {
            personaId = "alpha";
        }

        // persona.yaml が無ければ alpha へフォールバック（UIに数値は出さない）
        if (!AssetRepository.exists(this, AssetRepository.personaYaml(personaId))) {
            personaId = "alpha";
            CurrentPersonaStore.set(this, personaId);
        }

        PersonaMetaLoader.PersonaMeta meta =
                PersonaMetaLoader.load(this, personaId);

        if (meta == null) return;

        // required_visual_pack_id が無いと表示できないので、ここも安全化
        String visualPackId = meta.requiredVisualPackId;
        if (visualPackId == null || visualPackId.isEmpty()) {
            visualPackId = "michelle_default"; // 初期同梱前提
        }

        String skinId = SkinStore.get(
                this,
                personaId,
                meta.defaultSkin
        );

        String path = AssetRepository.visualSkinImage(
                visualPackId,
                skinId,
                "character_base.png"
        );

        Bitmap bmp = AssetRepository.loadBitmap(this, path);

        // skin が無い場合は default を試す（安全化）
        if (bmp == null && (skinId == null || !skinId.equals("default"))) {
            bmp = AssetRepository.loadBitmap(this,
                    AssetRepository.visualSkinImage(visualPackId, "default", "character_base.png"));
        }

        if (bmp != null) {
            personaCardImage.setImageBitmap(bmp);
        }

        PersonaEmotionApplier.applyBaseEmotion(this, personaId);

        if (personaName != null) {
            if (meta.displayName != null && !meta.displayName.isEmpty()) {
                personaName.setText(meta.displayName);
            } else {
                personaName.setText(personaId);
            }
        }
    }

    // =========================================================
    // 呼吸アニメ
    // =========================================================
    private void startBreathingAnimation() {

        if (personaCardImage == null) return;

        stopBreathingAnimation();

        breathingAnimator =
                ValueAnimator.ofFloat(1.0f, 1.015f);

        breathingAnimator.setDuration(2200);
        breathingAnimator.setRepeatCount(
                ValueAnimator.INFINITE);
        breathingAnimator.setRepeatMode(
                ValueAnimator.REVERSE);
        breathingAnimator.setInterpolator(
                new AccelerateDecelerateInterpolator());

        breathingAnimator.addUpdateListener(animation -> {
            float scale =
                    (float) animation.getAnimatedValue();
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

    // =========================================================
    // Worker
    // =========================================================
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
