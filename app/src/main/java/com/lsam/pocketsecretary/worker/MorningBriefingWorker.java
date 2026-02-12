package com.lsam.pocketsecretary.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.core.emotion.EmotionEngine;
import com.lsam.pocketsecretary.core.persona.PersonaToneProvider;
import com.lsam.pocketsecretary.core.settings.NotificationSettingsStore;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.service.NotificationService;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MorningBriefingWorker extends Worker {

    public static final String UNIQUE_WORK = "morning_briefing_once";

    public MorningBriefingWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();

        // OFFなら何もしない（ただし次回のOFF状態も維持したいので再スケジュールはしない）
        if (!NotificationSettingsStore.isMorningEnabled(context)) {
            return Result.success();
        }

        int todayCount = SimpleEventStore.countToday(context);

        EmotionEngine.Emotion emotion =
                EmotionEngine.evaluate(context);

        // 感情を反映（= ③ 実装）
        try {
            EmotionStateStore.getInstance().set(
                    mapEmotion(emotion)
            );
        } catch (Exception ignored) {}

        String persona = "kayama"; // 設定化は次フェーズ（今は落ちない固定）

        String message =
                PersonaToneProvider.buildMorningMessage(
                        context,
                        persona,
                        emotion,
                        todayCount
                );

        NotificationService service =
                new NotificationService(context);

        service.notifyAndRecord("morning", message);

        // 次回を再スケジュール（毎日 指定時刻）
        schedule(context);

        return Result.success();
    }

    public static void ensureScheduled(Context context) {
        // ONなら schedule、OFFなら何もしない
        if (NotificationSettingsStore.isMorningEnabled(context)) {
            schedule(context);
        }
    }

    public static void schedule(Context context) {

        Context app = context.getApplicationContext();

        int hour = NotificationSettingsStore.getMorningHour(app);
        int minute = NotificationSettingsStore.getMorningMinute(app);

        long delayMs = computeDelayMs(hour, minute);

        OneTimeWorkRequest req =
                new OneTimeWorkRequest.Builder(MorningBriefingWorker.class)
                        .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                        .addTag("ps_morning")
                        .build();

        WorkManager.getInstance(app).enqueueUniqueWork(
                UNIQUE_WORK,
                ExistingWorkPolicy.REPLACE,
                req
        );
    }

    private static long computeDelayMs(int hour, int minute) {

        Calendar now = Calendar.getInstance();

        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);

        if (!target.after(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1);
        }

        return target.getTimeInMillis() - now.getTimeInMillis();
    }

    private EmotionStateStore.Emotion mapEmotion(EmotionEngine.Emotion e) {
        if (e == EmotionEngine.Emotion.ALERT) return EmotionStateStore.Emotion.ALERT;
        if (e == EmotionEngine.Emotion.SPEAKING) return EmotionStateStore.Emotion.SPEAKING;
        if (e == EmotionEngine.Emotion.EVENING) return EmotionStateStore.Emotion.CALM;
        if (e == EmotionEngine.Emotion.FOCUS) return EmotionStateStore.Emotion.ALERT;
        return EmotionStateStore.Emotion.CALM;
    }
}
