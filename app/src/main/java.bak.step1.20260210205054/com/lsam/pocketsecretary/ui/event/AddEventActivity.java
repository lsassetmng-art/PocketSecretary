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
            // v2.x: stub (髣厄ｽｫ隴取得・ｽ・ｭ闔ｨ諛翫・鬨ｾ繝ｻ繝ｻ郢晢ｽｻ髯溷｢難ｽｪ雜｣・ｽ・ｶ陞｢・ｹ郢晢ｽｵ驛｢・ｧ繝ｻ・ｧ驛｢譎｢・ｽ・ｼ驛｢・ｧ繝ｻ・ｺ)
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            // v2.x: stub
            finish();
        });
    }
}
