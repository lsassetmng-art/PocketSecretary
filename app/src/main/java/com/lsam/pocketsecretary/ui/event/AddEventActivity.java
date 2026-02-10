package com.lsam.pocketsecretary.ui.event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private long selectedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        EditText edtTitle = findViewById(R.id.edtEventTitle);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        Button btnSave = findViewById(R.id.btnSaveEvent);

        Calendar cal = Calendar.getInstance();
        selectedAt = cal.getTimeInMillis();

        btnPickTime.setOnClickListener(v -> {
            // 最小：今の時刻を使う（後でPicker追加）
            selectedAt = Calendar.getInstance().getTimeInMillis();
            Toast.makeText(this, "現在時刻を設定しました", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleEventStore.add(this, title, selectedAt);
            Toast.makeText(this, "予定を保存しました", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
