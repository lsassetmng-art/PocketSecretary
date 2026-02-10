package com.lsam.pocketsecretary.ui.calendar;
import com.lsam.pocketsecretary.core.calendar.CalendarItem;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarReadActivity extends AppCompatActivity {
    private static final int REQ_CALENDAR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_read);

        TextView hint = findViewById(R.id.txtCalendarHint);
        hint.setText("Calendar (READ ONLY) — 今日/明日");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQ_CALENDAR
            );
        } else {
            loadEvents();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CALENDAR &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadEvents();
        }
    }

    private void loadEvents() {
        ListView listView = findViewById(R.id.listCalendar);
        List<String> items = new ArrayList<>();

        long now = System.currentTimeMillis();
        long until = now + 2L * 24L * 60L * 60L * 1000L; // 48h

        String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " <= ?";
        String[] args = new String[]{String.valueOf(now), String.valueOf(until)};

        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART },
                selection,
                args,
                CalendarContract.Events.DTSTART + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                long dtStart = cursor.getLong(1);
                String when = DateFormat.format("MM-dd HH:mm", new Date(dtStart)).toString();
                items.add(when + "  " + (title == null ? "(no title)" : title));
            }
            cursor.close();
        }

        // JP_COMMENT
        if (items.isEmpty()) {
            items.add("No events today.");
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }
}
