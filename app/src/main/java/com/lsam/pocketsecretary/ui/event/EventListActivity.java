// =========================================================
// app/src/main/java/com/lsam/pocketsecretary/ui/event/EventListActivity.java
// =========================================================
package com.lsam.pocketsecretary.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.core.schedule.EventScheduler;
import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EventListActivity extends BaseActivity {

    public static final String EXTRA_EVENT_ID = "event_id";

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    private enum Mode { MONTH, TODAY }
    private Mode mode = Mode.MONTH;

    private int viewYear;
    private int viewMonth0;

    private Button btnPrev;
    private Button btnNext;
    private Button btnToday;
    private Button btnMonth;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        Calendar now = Calendar.getInstance();
        viewYear = now.get(Calendar.YEAR);
        viewMonth0 = now.get(Calendar.MONTH);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);

        btnPrev = new Button(this); btnPrev.setText("<");
        btnNext = new Button(this); btnNext.setText(">");
        btnToday = new Button(this); btnToday.setText("Today");
        btnMonth = new Button(this); btnMonth.setText("Month");
        btnAdd = new Button(this); btnAdd.setText("Add");

        top.addView(btnPrev, weightLp());
        top.addView(btnNext, weightLp());
        top.addView(btnToday, weightLp());
        top.addView(btnMonth, weightLp());
        top.addView(btnAdd, weightLp());

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        root.addView(top, lp());
        root.addView(recyclerView, lp());

        setContentView(root);

        adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, EventEditActivity.class))
        );

        btnPrev.setOnClickListener(v -> {
            if (mode == Mode.MONTH) {
                shiftMonth(-1);
                loadEvents();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (mode == Mode.MONTH) {
                shiftMonth(1);
                loadEvents();
            }
        });

        btnToday.setOnClickListener(v -> {
            mode = Mode.TODAY;
            loadEvents();
        });

        btnMonth.setOnClickListener(v -> {
            mode = Mode.MONTH;
            loadEvents();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    private void loadEvents() {
        new Thread(() -> {

            List<EventEntity> list;

            if (mode == Mode.TODAY) {
                long[] range = computeTodayRangeMillis();
                list = EventDatabase.get(getApplicationContext())
                        .eventDao()
                        .listBetween(range[0], range[1]);
            } else {
                long[] range = computeMonthRangeMillis(viewYear, viewMonth0);
                list = EventDatabase.get(getApplicationContext())
                        .eventDao()
                        .listBetween(range[0], range[1]);
            }

            runOnUiThread(() -> adapter.setData(list));
        }).start();
    }

    private void shiftMonth(int delta) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, viewYear);
        c.set(Calendar.MONTH, viewMonth0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, delta);
        viewYear = c.get(Calendar.YEAR);
        viewMonth0 = c.get(Calendar.MONTH);
    }

    private long[] computeTodayRangeMillis() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long start = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_MONTH, 1);
        long end = c.getTimeInMillis();
        return new long[]{start, end};
    }

    private long[] computeMonthRangeMillis(int year, int month0) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long start = c.getTimeInMillis();
        c.add(Calendar.MONTH, 1);
        long end = c.getTimeInMillis();
        return new long[]{start, end};
    }

    private static LinearLayout.LayoutParams lp() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private static LinearLayout.LayoutParams weightLp() {
        return new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
    }

    @Override
    protected String getHeaderTitle() { return "Events"; }

    @Override
    protected boolean showSettingsButton() { return false; }

    private class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

        private List<EventEntity> data;

        void setData(List<EventEntity> list) {
            this.data = list;
            notifyDataSetChanged();
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout row = new LinearLayout(parent.getContext());
            row.setOrientation(LinearLayout.VERTICAL);
            return new EventViewHolder(row);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout container;

        EventViewHolder(LinearLayout view) {
            super(view);
            container = view;
        }

        void bind(EventEntity e) {

            container.removeAllViews();

            Button btnInfo = new Button(container.getContext());
            btnInfo.setText(e.title + " - " + formatTime(e.startAt, e.timeZone));

            Button btnDelete = new Button(container.getContext());
            btnDelete.setText("Delete");

            container.addView(btnInfo, lp());
            container.addView(btnDelete, lp());

            btnDelete.setOnClickListener(v -> {
                new Thread(() -> {

                    // ✅ AlarmManager cancel (current canonical)
                    EventScheduler.cancel(getApplicationContext(), e.id);

                    // ✅ WorkManager cancel (legacy safe)
                    WorkManager.getInstance(getApplicationContext())
                            .cancelUniqueWork("event_" + e.id);

                    // ✅ DB delete
                    EventDatabase.get(getApplicationContext())
                            .eventDao()
                            .deleteById(e.id);

                    loadEvents();

                }).start();
            });

            btnInfo.setOnClickListener(v -> {
                Intent intent = new Intent(EventListActivity.this, EventEditActivity.class);
                intent.putExtra(EXTRA_EVENT_ID, e.id);
                startActivity(intent);
            });
        }
    }

    private String formatTime(long millis, String tz) {
        TimeZone zone = TimeZone.getTimeZone(tz != null ? tz : "Asia/Tokyo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        sdf.setTimeZone(zone);
        return sdf.format(millis);
    }
}
