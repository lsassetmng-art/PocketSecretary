package com.lsam.pocketsecretary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.service.NotificationService;
import com.lsam.pocketsecretary.worker.ReminderWorker;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ManualNotificationActivity extends AppCompatActivity {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_notification);

        EditText inputMessage = findViewById(R.id.inputMessage);
        Button btnNotify = findViewById(R.id.btnNotify);
        Button btnNotifySpeak = findViewById(R.id.btnNotifySpeak);
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnHistory = findViewById(R.id.btnHistory);
        TimePicker timePicker = findViewById(R.id.timePicker);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.JAPAN);
            }
        });

        btnNotify.setOnClickListener(v ->
                notifyViaService("manual", inputMessage.getText().toString())
        );

        btnNotifySpeak.setOnClickListener(v -> {
            String msg = inputMessage.getText().toString();
            notifyViaService("manual", msg);
            speak(msg);
        });

        btnSchedule.setOnClickListener(v -> {

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

            long delay = target.getTimeInMillis() - now.getTimeInMillis();

            String msg = inputMessage.getText().toString();
            if (TextUtils.isEmpty(msg)) {
                msg = getString(R.string.notify_text_default);
            }

            Data data = new Data.Builder()
                    .putString("message", msg)
                    .build();

            OneTimeWorkRequest request =
                    new OneTimeWorkRequest.Builder(ReminderWorker.class)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(data)
                            .build();

            WorkManager.getInstance(this).enqueue(request);
        });

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );
    }

    private void notifyViaService(String source, String message) {
        NotificationService service =
                new NotificationService(this, null);
        service.notifyAndRecord(source, message);
    }

    private void speak(String text) {
        if (tts != null) {
            String t = TextUtils.isEmpty(text)
                    ? getString(R.string.notify_text_default)
                    : text;
            tts.speak(t, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) tts.shutdown();
        super.onDestroy();
    }
}