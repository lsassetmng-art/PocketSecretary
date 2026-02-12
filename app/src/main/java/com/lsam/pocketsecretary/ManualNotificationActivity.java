package com.lsam.pocketsecretary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.service.NotificationService;
import com.lsam.pocketsecretary.worker.ReminderWorker;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.core.notify.NotificationTextProvider;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ManualNotificationActivity extends BaseActivity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ BaseActivity統一
        setBaseContent(R.layout.activity_manual_notification);

        initAvatar();
        initUI();
        initTTS();
    }

    @Override
    protected String getHeaderTitle() {
        return "Manual Notify";
    }

    // ==============================
    // 初期化
    // ==============================

    private void initAvatar() {
        ImageView avatarImage = findViewById(R.id.avatarImage);
        updateAvatar(
                EmotionStateStore.getInstance()
                        .current()
                        .name()
                        .toLowerCase()
        );
    }

    private void initTTS() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.JAPAN);
            }
        });
    }

    private void initUI() {

        EditText inputMessage = findViewById(R.id.inputMessage);
        Button btnNotify = findViewById(R.id.btnNotify);
        Button btnNotifySpeak = findViewById(R.id.btnNotifySpeak);
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnHistory = findViewById(R.id.btnHistory);
        TimePicker timePicker = findViewById(R.id.timePicker);

        // 即通知
        btnNotify.setOnClickListener(v ->
                notifyViaService("manual",
                        inputMessage.getText().toString())
        );

        // 通知＋読み上げ
        btnNotifySpeak.setOnClickListener(v -> {
            String msg = inputMessage.getText().toString();
            notifyViaService("manual", msg);
            speak(msg);
        });

        // 予約通知
        btnSchedule.setOnClickListener(v ->
                scheduleNotification(inputMessage, timePicker)
        );

        // 履歴
        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this,
                        com.lsam.pocketsecretary.ui.history.HistoryActivity.class))
        );
    }

    // ==============================
    // 予約処理
    // ==============================

    private void scheduleNotification(EditText inputMessage,
                                      TimePicker timePicker) {

        int hour;
        int minute;

        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);
        target.set(Calendar.SECOND, 0);

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_MONTH, 1);
        }

        long delay =
                target.getTimeInMillis() - now.getTimeInMillis();

        String msg = buildMessage(inputMessage.getText().toString());

        Data data = new Data.Builder()
                .putString("message", msg)
                .build();

        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();

        WorkManager.getInstance(this).enqueue(request);
    }

    private String buildMessage(String input) {

        String msg = input;

        if (TextUtils.isEmpty(msg)) {
            msg = getString(R.string.notify_text_default);

            int todayCount =
                    SimpleEventStore.countToday(this);

            boolean tight = todayCount >= 3;
            boolean cont = false;

            String hint =
                    NotificationTextProvider.hint(this, tight, cont);

            if (!hint.isEmpty()) {
                msg = msg + "\n" + hint;
            }
        }

        return msg;
    }

    // ==============================
    // 通知・音声
    // ==============================

    private void notifyViaService(String source, String message) {
        NotificationService service =
                new NotificationService(this);
        service.notifyAndRecord(source, message);
    }

    private void speak(String text) {
        if (tts != null) {
            String t = TextUtils.isEmpty(text)
                    ? getString(R.string.notify_text_default)
                    : text;

            tts.speak(t,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }

    // ==============================
    // アバター
    // ==============================

    private void updateAvatar(String emotion) {

        ImageView avatar = findViewById(R.id.avatarImage);

        String fileName = "avatar_calm.png";

        if ("alert".equals(emotion)) {
            fileName = "avatar_alert.png";
        } else if ("speaking".equals(emotion)) {
            fileName = "avatar_speaking.png";
        }

        try {
            InputStream is = getAssets().open(fileName);
            avatar.setImageBitmap(
                    android.graphics.BitmapFactory.decodeStream(is)
            );
        } catch (Exception ignored) {
        }
    }
}
