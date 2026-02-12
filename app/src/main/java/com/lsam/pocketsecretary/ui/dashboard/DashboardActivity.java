package com.lsam.pocketsecretary.ui.dashboard;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.dashboard.TodayEngine;
import com.lsam.pocketsecretary.persona.EmotionStateStore;
import com.lsam.pocketsecretary.core.persona.PersonaLoader;
import com.lsam.pocketsecretary.core.persona.PersonaEmotionApplier;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;
import com.lsam.pocketsecretary.ui.persona.PersonaSelectBottomSheet;

import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private ImageView bg;
    private TextView personaName;
    private TextView count;
    private TextView next;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_dashboard_persona);

        bg = findViewById(R.id.psPersonaBackground);
        personaName = findViewById(R.id.txtPersonaName);
        count = findViewById(R.id.txtTodayCount);
        next  = findViewById(R.id.txtNextEvent);

        findViewById(R.id.btnSpeechTool).setOnClickListener(v ->
                startActivity(new Intent(this,
                        com.lsam.pocketsecretary.ui.speech.SpeechToolActivity.class))
        );

        personaName.setOnClickListener(v -> {

            PersonaSelectBottomSheet sheet =
                    new PersonaSelectBottomSheet(personaId -> {

                        CurrentPersonaStore.set(this, personaId);
                        applyCurrentPersona();
                    });

            sheet.show(getSupportFragmentManager(), "persona_select");
        });

        renderTodayInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyCurrentPersona();
    }

    private void renderTodayInfo() {

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
    }

    private void applyCurrentPersona() {

        String personaId = CurrentPersonaStore.get(this);

        Bitmap bmp = PersonaLoader.loadPersonaImage(this, personaId);
        if (bmp != null && bg != null) {
            bg.setImageBitmap(bmp);
        }

        PersonaEmotionApplier.applyBaseEmotion(this, personaId);

        Map<String, String> personaMap =
                PersonaYamlLoader.load(this, personaId);

        String displayName = personaMap.get("display_name");

        if (displayName != null && personaName != null) {
            personaName.setText(displayName);
        }
    }
}