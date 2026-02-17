package com.lsam.pocketsecretary.ui.event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;
import com.lsam.pocketsecretary.core.schedule.EventScheduler;

public class EventDetailActivity extends BaseActivity {

    public static final String EXTRA_EVENT_ID = "event_id";

    private TextView txtTitle;
    private TextView txtTime;

    private String eventId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        txtTitle = new TextView(this);
        txtTime = new TextView(this);

        Button btnEdit = new Button(this);
        btnEdit.setText("Edit");

        Button btnDelete = new Button(this);
        btnDelete.setText("Delete");

        root.addView(txtTitle);
        root.addView(txtTime);
        root.addView(btnEdit);
        root.addView(btnDelete);

        setContentView(root);

        load();

        btnDelete.setOnClickListener(v -> delete());
    }

    private void load() {
        new Thread(() -> {
            EventEntity e =
                EventDatabase.get(getApplicationContext())
                    .eventDao()
                    .findById(eventId);

            if (e == null) return;

            runOnUiThread(() -> {
                txtTitle.setText(e.title);
                txtTime.setText(String.valueOf(e.startAt));
            });
        }).start();
    }

    private void delete() {
        new Thread(() -> {

            EventScheduler.cancel(getApplicationContext(), eventId);

            EventDatabase.get(getApplicationContext())
                .eventDao()
                .deleteById(eventId);

            runOnUiThread(this::finish);

        }).start();
    }

    @Override
    protected String getHeaderTitle() {
        return "Event Detail";
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}