package com.lsam.pocketsecretary;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.history.NotificationHistoryEntity;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private static final int LIMIT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle(getString(R.string.history_title));

        TextView textView = findViewById(R.id.historyText);

        List<NotificationHistoryEntity> list =
                NotificationHistoryStore.get(this).latestBlocking(LIMIT);

        if (list == null || list.isEmpty()) {
            textView.setText(getString(R.string.history_empty));
            return;
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        StringBuilder sb = new StringBuilder();

        for (NotificationHistoryEntity e : list) {
            sb.append(fmt.format(e.createdAtEpochMs))
              .append(" / ")
              .append(e.source)
              .append("\n")
              .append(e.title)
              .append("\n")
              .append(e.text)
              .append("\n\n");
        }

        textView.setText(sb.toString());
    }
}