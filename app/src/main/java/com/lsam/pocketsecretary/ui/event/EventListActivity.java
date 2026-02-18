package com.lsam.pocketsecretary.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.base.BaseActivity;
import com.lsam.pocketsecretary.data.event.EventEntity;
import com.lsam.pocketsecretary.data.event_ui.EventUiRepository;
import com.lsam.pocketsecretary.data.todo.TodoRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListActivity extends BaseActivity {

    public static final String EXTRA_EVENT_ID = "extra_event_id";

    private enum Mode { TODAY, MONTH }

    private Mode mode = Mode.TODAY;

    private CalendarView calendarView;
    private TextView emptyView;

    private EventUiRepository eventRepo;
    private TodoRepository todoRepo;

    private EventAdapter adapter;
    private final List<EventEntity> current = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Button btnToday = findViewById(R.id.btnToday);
        Button btnMonth = findViewById(R.id.btnMonth);
        calendarView = findViewById(R.id.calendarView);
        emptyView = findViewById(R.id.emptyView);

        eventRepo = new EventUiRepository(this);
        todoRepo = new TodoRepository(this);

        RecyclerView rv = findViewById(R.id.eventRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventAdapter(this, current, e -> openEdit(e.id));
        rv.setAdapter(adapter);

        btnToday.setOnClickListener(v -> {
            mode = Mode.TODAY;
            calendarView.setVisibility(View.GONE);
            loadForToday();
        });

        btnMonth.setOnClickListener(v -> {
            mode = Mode.MONTH;
            calendarView.setVisibility(View.VISIBLE);
            loadForDay(calendarView.getDate());
        });

        calendarView.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth, 0, 0, 0);
            c.set(Calendar.MILLISECOND, 0);
            loadForDay(c.getTimeInMillis());
        });

        loadForToday();
    }

    private void loadForToday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        loadForDay(c.getTimeInMillis());
    }

    private void loadForDay(long anyTimeInDayMs) {
        long start = dayStart(anyTimeInDayMs);
        long end = start + 24L * 60L * 60L * 1000L;

        eventRepo.getEventsForDay(start, end, new EventUiRepository.Callback<List<EventEntity>>() {
            @Override
            public void onSuccess(List<EventEntity> events) {
                runOnUiThread(() -> {
                    current.clear();
                    if (events != null) {
                        current.addAll(events);
                    }
                    adapter.update(current);
                    emptyView.setVisibility(current.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> emptyView.setVisibility(View.VISIBLE));
            }
        });
    }

    private long dayStart(long ms) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ms);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    private void openEdit(String eventId) {
        Intent i = new Intent(this, EventEditActivity.class);
        i.putExtra(EXTRA_EVENT_ID, eventId);
        startActivity(i);
    }
}
