package com.lsam.pocketsecretary.ui.secretary;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.NotificationScheduler;
import com.lsam.pocketsecretary.core.prefs.Prefs;
import com.lsam.pocketsecretary.core.secretary.Secretary;
import com.lsam.pocketsecretary.core.secretary.SecretaryCatalog;
import com.lsam.pocketsecretary.core.secretary.SecretarySpeech;

import java.util.Date;

public class SecretaryChatActivity extends AppCompatActivity {

    public static final String EXTRA_SECRETARY_ID = "secretary_id";

    private TextView title;
    private TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_chat);

        title = findViewById(R.id.txtChatTitle);
        body  = findViewById(R.id.txtChatBody);
        Button btnNotifyNow = findViewById(R.id.btnChatNotifyNow);

        btnNotifyNow.setOnClickListener(v -> {
            if (!Prefs.isNotifyEnabled(this)) return;
            Secretary s = SecretaryCatalog.byId(Prefs.getDefaultSecretary(this));
            String notifyText = SecretarySpeech.notifyText(s, "確認が必要な予定があるよ");
            NotificationScheduler.fireNow(this, "PocketSecretary", notifyText);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // JP_COMMENT
        String id = getIntent().getStringExtra(EXTRA_SECRETARY_ID);
        if (id == null || id.isEmpty()) {
            id = Prefs.getDefaultSecretary(this);
        }
        Secretary s = SecretaryCatalog.byId(id);

        title.setText(s.name + "（" + s.tone + "）");

        String next = loadNextEventLine();
        body.setText(
                SecretarySpeech.greet(s) + "\n\n" +
                SecretarySpeech.planLine(s, next)
        );
    }

    private String loadNextEventLine() {
        long now = System.currentTimeMillis();
        long until = now + 24L * 60L * 60L * 1000L;

        String selection = CalendarContract.Events.DTSTART + " >= ? AND " +
                CalendarContract.Events.DTSTART + " <= ?";
        String[] args = new String[]{String.valueOf(now), String.valueOf(until)};

        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART
                },
                selection,
                args,
                CalendarContract.Events.DTSTART + " ASC"
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String title = cursor.getString(0);
                    long dtStart = cursor.getLong(1);
                    String when = DateFormat.format("MM-dd HH:mm", new Date(dtStart)).toString();
                    return when + "  " + (title == null ? "(no title)" : title);
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }
}
