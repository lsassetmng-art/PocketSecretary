package com.lsam.pocketsecretary.ui.speech;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.core.voice.VoiceManager;

public class SpeechToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_speech_tool);

        TextView status = findViewById(R.id.txtResearchStatus);

        int today = TodayEngine.todayCount(this);
        boolean tight = TodayEngine.isTight(this);

        EmotionStateStore store = EmotionStateStore.getInstance();
        EmotionStateStore.Emotion emotion = store.current();

        String s = "Today: " + today +
                " / Tight: " + tight +
                " / Emotion: " + emotion.name();

        status.setText(s);

        EditText input = findViewById(R.id.editSpeechText);
        SeekBar rate = findViewById(R.id.seekRate);
        SeekBar pitch = findViewById(R.id.seekPitch);
        Button play = findViewById(R.id.btnPlay);

        rate.setProgress(100);
        pitch.setProgress(100);

        play.setOnClickListener(v -> {
            String text = input.getText().toString();
            float r = rate.getProgress() / 100f;
            float p = pitch.getProgress() / 100f;

            VoiceManager.configure(r, p);
            VoiceManager.speak(this, text);
        });
    }
}
