package com.lsam.pocketsecretary.ui.consult;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.consult.*;
import com.lsam.pocketsecretary.core.voice.VoiceManager;

public class ConsultActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_consult);

        TextView out = findViewById(R.id.txtAnswer);

        set(R.id.btnBusy, QuestionType.TODAY_BUSY, out);
        set(R.id.btnNext, QuestionType.NEXT_EVENT, out);
        set(R.id.btnFree, QuestionType.FREE_TIME, out);
        set(R.id.btnGo,   QuestionType.SHOULD_GO_NOW, out);
    }
    private void set(int id, QuestionType q, TextView out){
        Button b = findViewById(id);
        b.setOnClickListener(v->{
            String a = ConsultEngine.answer(this, q);
            out.setText(a);
            VoiceManager.speak(this, a);
        });
    }
}
