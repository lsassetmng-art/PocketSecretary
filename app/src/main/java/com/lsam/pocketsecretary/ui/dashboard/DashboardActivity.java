package com.lsam.pocketsecretary.ui.dashboard;

import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.persona.EmotionStateStore;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_dashboard);

        TextView count = findViewById(R.id.txtTodayCount);
        TextView next  = findViewById(R.id.txtNextEvent);

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

        findViewById(R.id.btnSpeechTool).setOnClickListener(v -> {
            startActivity(new Intent(this,
                com.lsam.pocketsecretary.ui.speech.SpeechToolActivity.class));
        });
    }
}
