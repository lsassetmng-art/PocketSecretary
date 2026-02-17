package com.lsam.pocketsecretary.ui.event;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;
import com.lsam.pocketsecretary.worker.EventNotificationWorker;

import java.util.Locale;
import java.util.UUID;

public class EventEditActivity extends BaseActivity {

    private EditText edtTitle;
    private EditText edtStart;
    private EditText edtInterval;
    private EditText edtCount;
    private EditText edtUntil;
    private Spinner spFreq;

    private String editingEventId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        editingEventId = getIntent() != null
                ? getIntent().getStringExtra(EventListActivity.EXTRA_EVENT_ID)
                : null;

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        edtTitle = new EditText(this);
        edtTitle.setHint("Title");

        edtStart = new EditText(this);
        edtStart.setHint("Start epoch millis");

        spFreq = new Spinner(this);
        String[] freqs = {"NONE", "DAILY", "WEEKLY", "MONTHLY", "YEARLY"};
        spFreq.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                freqs
        ));

        edtInterval = new EditText(this);
        edtInterval.setHint("Interval");

        edtCount = new EditText(this);
        edtCount.setHint("Count");

        edtUntil = new EditText(this);
        edtUntil.setHint("Until epoch millis");

        Button btnSave = new Button(this);
        btnSave.setText("Save");

        root.addView(edtTitle, lp());
        root.addView(edtStart, lp());
        root.addView(spFreq, lp());
        root.addView(edtInterval, lp());
        root.addView(edtCount, lp());
        root.addView(edtUntil, lp());
        root.addView(btnSave, lp());

        setContentView(root);

        if (!TextUtils.isEmpty(editingEventId)) {
            loadForEdit(editingEventId);
        }

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

                String freq = "NONE";
                String interval = "";
                if (e.recurrenceRule != null) {
                    String rule = e.recurrenceRule;
                    String f = extractValue(rule, "FREQ");
                    if (!TextUtils.isEmpty(f)) freq = f;

                    String it = extractValue(rule, "INTERVAL");
                    if (!TextUtils.isEmpty(it)) interval = it;
                }

                setSpinnerValue(spFreq, freq);
                edtInterval.setText(interval);

                if (e.recurrenceCount != null) {
                    edtCount.setText(String.valueOf(e.recurrenceCount));
                }

                if (e.recurrenceUntil != null) {
                    edtUntil.setText(String.valueOf(e.recurrenceUntil));
                }
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

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(startStr)) {
            return;
        }

        final long startAt;
        try {
            startAt = Long.parseLong(startStr);
        } catch (Exception e) {
            return;
        }

        final String freq = spFreq.getSelectedItem() != null
                ? spFreq.getSelectedItem().toString()
                : "NONE";

        final String interval = edtInterval.getText() != null
                ? edtInterval.getText().toString().trim()
                : "";

        final String countStr = edtCount.getText() != null
                ? edtCount.getText().toString().trim()
                : "";

        final String untilStr = edtUntil.getText() != null
                ? edtUntil.getText().toString().trim()
                : "";

        new Thread(() -> {

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
            e.startAt = startAt;
            e.endAt = startAt + 60_000L;
            e.allDay = false;
            e.timeZone = "Asia/Tokyo";
            e.reminderBeforeMin = 10;
            e.localOnly = true;

            e.recurrenceRule = null;
            e.recurrenceCount = null;
            e.recurrenceUntil = null;

            if (!"NONE".equals(freq)) {
                StringBuilder r = new StringBuilder();
                r.append("FREQ=").append(freq);
                if (!TextUtils.isEmpty(interval)) {
                    r.append(";INTERVAL=").append(interval);
                }
                e.recurrenceRule = r.toString();

                if (!TextUtils.isEmpty(countStr)) {
                    try {
                        e.recurrenceCount = Integer.parseInt(countStr);
                    } catch (Exception ignored) {
                        e.recurrenceCount = null;
                    }
                }

                if (!TextUtils.isEmpty(untilStr)) {
                    try {
                        e.recurrenceUntil = Long.parseLong(untilStr);
                    } catch (Exception ignored) {
                        e.recurrenceUntil = null;
                    }
                }
            }

            EventDatabase.get(getApplicationContext())
                    .eventDao()
                    .upsert(e);

            long notifyAt =
                    e.startAt - (long) e.reminderBeforeMin * 60_000L;

            if (notifyAt < System.currentTimeMillis()) {
                notifyAt = System.currentTimeMillis() + 1000L;
            }

            EventNotificationWorker.scheduleNext(
                    getApplicationContext(),
                    e.id,
                    notifyAt
            );

            runOnUiThread(this::finish);
        }).start();
    }

    private static String extractValue(String rule, String key) {
        if (rule == null) return "";
        String[] parts = rule.split(";");
        String prefix = key + "=";
        for (String p : parts) {
            if (p != null && p.startsWith(prefix)) {
                return p.substring(prefix.length()).trim();
            }
        }
        return "";
    }

    private static void setSpinnerValue(Spinner sp, String value) {
        if (sp == null || value == null) return;
        for (int i = 0; i < sp.getCount(); i++) {
            Object item = sp.getItemAtPosition(i);
            if (item != null && value.equals(item.toString())) {
                sp.setSelection(i);
                return;
            }
        }
    }

    private static LinearLayout.LayoutParams lp() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    protected String getHeaderTitle() {
        return "Event";
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
