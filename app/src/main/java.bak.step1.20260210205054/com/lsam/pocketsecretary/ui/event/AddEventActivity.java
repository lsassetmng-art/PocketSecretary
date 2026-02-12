package com.lsam.pocketsecretary.ui.event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        EditText edtTitle = findViewById(R.id.edtTitle);
        Button btnSave = findViewById(R.id.btnSaveEvent);
        Button btnDelete = findViewById(R.id.btnDeleteEvent);

        btnSave.setOnClickListener(v -> {
            // v2.x: stub (菫晏ｭ伜・逅・・蠕檎ｶ壹ヵ繧ｧ繝ｼ繧ｺ)
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            // v2.x: stub
            finish();
        });
    }
}