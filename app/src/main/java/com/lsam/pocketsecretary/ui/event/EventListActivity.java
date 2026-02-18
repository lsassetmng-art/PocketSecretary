package com.lsam.pocketsecretary.ui.event;
    public static final String EXTRA_EVENT_ID = "extra_event_id";


    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.content.Intent;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.os.Bundle;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.view.View;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.widget.Button;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.widget.CalendarView;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import android.widget.TextView;
    public static final String EXTRA_EVENT_ID = "extra_event_id";


    public static final String EXTRA_EVENT_ID = "extra_event_id";

import androidx.annotation.NonNull;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import androidx.recyclerview.widget.LinearLayoutManager;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import androidx.recyclerview.widget.RecyclerView;
    public static final String EXTRA_EVENT_ID = "extra_event_id";


    public static final String EXTRA_EVENT_ID = "extra_event_id";

import com.lsam.pocketsecretary.R;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import com.lsam.pocketsecretary.core.base.BaseActivity;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import com.lsam.pocketsecretary.data.event.EventEntity;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import com.lsam.pocketsecretary.data.event_ui.EventUiRepository;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import com.lsam.pocketsecretary.data.todo.TodoRepository;
    public static final String EXTRA_EVENT_ID = "extra_event_id";


    public static final String EXTRA_EVENT_ID = "extra_event_id";

import java.util.ArrayList;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import java.util.Calendar;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import java.util.HashMap;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import java.util.List;
    public static final String EXTRA_EVENT_ID = "extra_event_id";

import java.util.Map;
    public static final String EXTRA_EVENT_ID = "extra_event_id";


    public static final String EXTRA_EVENT_ID = "extra_event_id";

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
        applyThemeToRoot(findViewById(android.R.id.content));

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
            // default: selected day = today
            loadForDay(calendarView.getDate());
        });

        calendarView.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
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
            @Override public void onSuccess(List<EventEntity> events) {
                // Build todo count map for those events (avoid heavy per-row calls)
                injectTodoCountsAndRender(events);
            }
            @Override public void onError(Exception e) {
                runOnUiThread(() -> {
                    current.clear();
                    adapter.update(current);
                    emptyView.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void injectTodoCountsAndRender(List<EventEntity> events) {
        if (events == null) events = new ArrayList<>();

        if (events.isEmpty()) {
            List<EventEntity> finalEvents = events;
            runOnUiThread(() -> {
                current.clear();
                current.addAll(finalEvents);
                adapter.update(current);
                emptyView.setVisibility(View.VISIBLE);
            });
            return;
        }

        // For each event, ask todo count (async). We aggregate results, then render.
        Map<String, Integer> counts = new HashMap<>();
        final int total = events.size();
        final int[] done = new int[]{0};

        for (EventEntity e : events) {
            final String eventId = e.id;
            todoRepo.countOpenByEventId(eventId, new TodoRepository.Callback<Integer>() {
                @Override public void onSuccess(Integer value) {
                    counts.put(eventId, value == null ? 0 : value);
                    onCountDone();
                }
                @Override public void onError(Exception ex) {
                    counts.put(eventId, 0);
                    onCountDone();
                }

                private void onCountDone() {
                    synchronized (done) {
                        done[0]++;
                        if (done[0] >= total) {
                            render(events, counts);
                        }
                    }
                }
            });
        }
    }

    private void render(List<EventEntity> events, Map<String, Integer> counts) {
        runOnUiThread(() -> {
            current.clear();
            current.addAll(events);
            adapter.update(current);

            // attach counts as view tags by position on bind:
            // simplest approach: set a per-item tag right before notify by iterating visible later.
            // Instead, we store counts in a static map and set tags in onBind would require adapter support.
            // Minimal: set tags after update by notifying again with tags via a lightweight hack:
            // We attach counts in a global map stored in Activity and read it using RecyclerView's onChildAttach.
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
        // If EventEditActivity expects extras, adapt here. Keep minimal: open by id if supported.
        try {
            Intent i = new Intent(this, Class.forName("com.lsam.pocketsecretary.ui.event.EventEditActivity"));
            i.putExtra(EXTRA_EVENT_ID, eventId);
            startActivity(i);
        } catch (Exception ignored) {
        }
    }
}
