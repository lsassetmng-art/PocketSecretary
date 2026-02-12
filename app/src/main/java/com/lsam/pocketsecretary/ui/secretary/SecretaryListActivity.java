package com.lsam.pocketsecretary.ui.secretary;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(this);
        tv.setText("PocketSecretary - Phase E OK");
        tv.setTextSize(20f);
        tv.setPadding(40, 100, 40, 40);

        layout.addView(tv);

        setContentView(layout);
    }
}
