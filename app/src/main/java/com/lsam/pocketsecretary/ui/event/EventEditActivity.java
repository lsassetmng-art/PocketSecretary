package com.lsam.pocketsecretary.ui.event;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.schedule.EventScheduler;
import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;

import java.util.UUID;

public class EventEditActivity extends BaseActivity {

    private EditText edtTitle;
    private EditText edtStart;
    private Spinner spFreq;
    private EditText edtInterval;
    private EditText edtCount;
    private EditText edtUntil;

    private String editingEventId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_event_edit);

        editingEventId = getIntent() != null
                ? getIntent().getStringExtra(EventListActivity.EXTRA_EVENT_ID)
                : null;

        bindViews();

        if (!TextUtils.isEmpty(editingEventId)) {
            loadForEdit(editingEventId);
        }
    }

    private void bindViews() {

        edtTitle = findViewById(R.id.edtTitle);
        edtStart = findViewById(R.id.edtStart);
        spFreq = findViewById(R.id.spFreq);
        edtInterval = findViewById(R.id.edtInterval);
        edtCount = findViewById(R.id.edtCount);
        edtUntil = findViewById(R.id.edtUntil);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> save());
    }

    private void loadForEdit(String id) {

        new Thread(() -> {

            EventEntity e = EventDatabase.get(getApplicationContext())
                    .eventDao()
                    .findById(id);

            if (e == null) return;

            runOnUiThread(() -> {
                edtTitle.setText(e.title != null ? e.title : "");
                edtStart.setText(String.valueOf(e.startAt));
            });

        }).start();
    }

    private void save() {

        final String title = edtTitle.getText() != null
                ? edtTitle.getText().toString().trim()
                : "";

        final String startStr = edtStart.getText() != null
                ? edtStart.getText().toString().trim()
                : "";

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this,
                    getString(R.string.ps_event_title_required),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        long startAt;

        if (TextUtils.isEmpty(startStr)) {
            startAt = System.currentTimeMillis();
        } else {
            try {
                startAt = Long.parseLong(startStr);
            } catch (Exception e) {
                startAt = System.currentTimeMillis();
            }
        }

        final long finalStartAt = startAt;

        new Thread(() -> {

            try {

                EventEntity e;

                if (!TextUtils.isEmpty(editingEventId)) {
                    e = EventDatabase.get(getApplicationContext())
                            .eventDao()
                            .findById(editingEventId);
                    if (e == null) {
                        e = new EventEntity();
                        e.id = editingEventId;
                    }
                } else {
                    e = new EventEntity();
                    e.id = UUID.randomUUID().toString();
                }

                e.title = title;
                e.startAt = finalStartAt;
                e.endAt = finalStartAt + 60_000L;
                e.allDay = false;
                e.timeZone = "Asia/Tokyo";
                e.reminderBeforeMin = 10;
                e.localOnly = true;
                e.lastOccurrenceAt = null;

                EventDatabase.get(getApplicationContext())
                        .eventDao()
                        .upsert(e);

                long notifyAt =
                        e.startAt - (long) e.reminderBeforeMin * 60_000L;

                if (notifyAt < System.currentTimeMillis()) {
                    notifyAt = System.currentTimeMillis() + 1000L;
                }

                scheduleSafe(e.id, notifyAt);

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            getString(R.string.ps_event_saved),
                            Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception ex) {

                runOnUiThread(() ->
                        Toast.makeText(this,
                                getString(R.string.ps_event_save_error, ex.getMessage()),
                                Toast.LENGTH_LONG).show()
                );

                ex.printStackTrace();
            }

        }).start();
    }

    private void scheduleSafe(String eventId, long notifyAt) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager am =
                        (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (am != null && !am.canScheduleExactAlarms()) {
                    EventScheduler.scheduleInexact(
                            getApplicationContext(),
                            eventId,
                            notifyAt
                    );
                    return;
                }
            }

            EventScheduler.scheduleExact(
                    getApplicationContext(),
                    eventId,
                    notifyAt
            );

        } catch (SecurityException se) {
            try {
                EventScheduler.scheduleInexact(
                        getApplicationContext(),
                        eventId,
                        notifyAt
                );
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_screen_event);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
