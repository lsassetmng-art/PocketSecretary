package com.lsam.pocketsecretary.ui.calendar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarReadActivity extends AppCompatActivity {

    private static final int REQ_CALENDAR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_read);

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

        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART
                },
                null,
                null,
                CalendarContract.Events.DTSTART + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                items.add(title);
            }
            cursor.close();
        }

        listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items)
        );
    }
}
