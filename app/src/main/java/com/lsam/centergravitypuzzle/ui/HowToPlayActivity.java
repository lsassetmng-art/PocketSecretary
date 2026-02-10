package com.lsam.centergravitypuzzle;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HowToPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);

        TextView t = findViewById(R.id.txtHow);
        t.setText(
            "JP_TEXTーJP_TEXT（JP_TEXT）\n\n" +
            "・JP_TEXT、JP_TEXT（↑↓←→）\n" +
            "・JP_TEXT、JP_TEXT\n" +
            "・JP_TEXT/JP_TEXT3JP_TEXT（L/TJP_TEXT）\n" +
            "・JP_TEXTーJP_TEXTーJP_TEXTー\n\n" +
            "Phase5: JP_TEXT『JP_TEXT』JP_TEXT"
        );
    }
}
