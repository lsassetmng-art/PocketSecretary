package com.lsam.pocketsecretary.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.core.settings.NotificationSettingsStore;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.core.emotion.EmotionEngine;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.service.NotificationService;

public class UpcomingEventWorker extends Worker {

    public UpcomingEventWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();

        // 予定前通知 minutes（= 設定画面で可変）
        int beforeMin = NotificationSettingsStore.getReminderBeforeMinutes(context);

        // ---- NOTE ----
        // 現時点で「次予定の開始時刻」を取得するAPIがプロジェクト側に存在しない可能性があるため、
        // ここは"落ちない"下地にしている。
        // TodayEngine 側に nextStartMillis() 等が来たら、ここで
        //  (nextStartMillis - now) <= beforeMin*60*1000 の判定を入れて完成させる。

        String nextTitle = TodayEngine.nextTitle(context);
        if (nextTitle == null) nextTitle = "";

        // 次予定が無いなら何もしない（静か）
        if (nextTitle.trim().isEmpty()) {
            return Result.success();
        }

        // 感情を反映（= ③ 実装）
        try {
            EmotionStateStore.getInstance().set(EmotionStateStore.Emotion.SPEAKING);
        } catch (Exception ignored) {}

        String msg = "次の予定が近づいています（" + beforeMin + "分前目安）\n" + nextTitle;

        NotificationService service =
                new NotificationService(context);

        // source は "upcoming" で履歴に残る
        service.notifyAndRecord("upcoming", msg);

        return Result.success();
    }
}
