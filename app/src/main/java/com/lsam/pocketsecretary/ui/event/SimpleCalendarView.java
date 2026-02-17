package com.lsam.pocketsecretary.ui.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * SimpleCalendarView
 * - 軽量の月表示（7列）
 * - 日付クリックで epoch millis(その日の00:00) を返す
 * - ここでは「見た目より動作」を優先（Phase Fの土台）
 */
public class SimpleCalendarView extends LinearLayout {

    public interface OnDateSelectedListener {
        void onSelected(long dayStartMillis);
    }

    private final TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");

    private TextView txtMonth;
    private GridLayout grid;

    private int year;
    private int month0; // 0-based

    private OnDateSelectedListener listener;

    public SimpleCalendarView(Context context) {
        super(context);
        init();
    }

    public SimpleCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        txtMonth = new TextView(getContext());
        txtMonth.setGravity(Gravity.CENTER);
        txtMonth.setTextSize(16f);

        addView(txtMonth, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // weekday header
        GridLayout header = new GridLayout(getContext());
        header.setColumnCount(7);

        String[] w = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for (String s : w) {
            TextView t = new TextView(getContext());
            t.setText(s);
            t.setGravity(Gravity.CENTER);
            header.addView(t, cellLp());
        }
        addView(header, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        grid = new GridLayout(getContext());
        grid.setColumnCount(7);
        addView(grid, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        Calendar now = Calendar.getInstance(tz);
        setMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH));
    }

    private GridLayout.LayoutParams cellLp() {
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = 0;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        lp.setGravity(Gravity.FILL_HORIZONTAL);
        return lp;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener l) {
        this.listener = l;
    }

    public void setMonth(int year, int month0) {
        this.year = year;
        this.month0 = month0;

        txtMonth.setText(String.format(
                Locale.US,
                "%04d-%02d",
                year,
                month0 + 1
        ));

        render();
    }

    public int getYear() {
        return year;
    }

    public int getMonth0() {
        return month0;
    }

    private void render() {
        grid.removeAllViews();

        Calendar first = Calendar.getInstance(tz);
        first.set(Calendar.YEAR, year);
        first.set(Calendar.MONTH, month0);
        first.set(Calendar.DAY_OF_MONTH, 1);
        first.set(Calendar.HOUR_OF_DAY, 0);
        first.set(Calendar.MINUTE, 0);
        first.set(Calendar.SECOND, 0);
        first.set(Calendar.MILLISECOND, 0);

        int firstDow = first.get(Calendar.DAY_OF_WEEK); // 1=Sun
        int leading = firstDow - Calendar.SUNDAY; // 0..6

        Calendar cursor = (Calendar) first.clone();
        cursor.add(Calendar.DAY_OF_MONTH, -leading);

        // 6週分（最大42セル）
        for (int i = 0; i < 42; i++) {
            final long dayStart = cursor.getTimeInMillis();
            final int day = cursor.get(Calendar.DAY_OF_MONTH);
            final boolean inMonth = (cursor.get(Calendar.MONTH) == month0);

            Button b = new Button(getContext());
            b.setText(String.valueOf(day));
            b.setAllCaps(false);

            // 月外は薄く
            b.setAlpha(inMonth ? 1.0f : 0.35f);

            b.setOnClickListener(v -> {
                if (listener != null) listener.onSelected(dayStart);
            });

            grid.addView(b, cellLp());
            cursor.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}