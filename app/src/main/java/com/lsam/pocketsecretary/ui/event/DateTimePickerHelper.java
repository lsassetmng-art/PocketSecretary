package com.lsam.pocketsecretary.ui.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * DateTimePickerHelper
 * - epoch millis 入力を廃止するための補助
 * - DatePicker → TimePicker の順で選ばせて epoch millis を返す
 * - タイムゾーンは原則 Asia/Tokyo
 */
public final class DateTimePickerHelper {

    public interface Callback {
        void onPicked(long epochMillis);
    }

    private DateTimePickerHelper() {}

    public static void pickDateTime(
            Context context,
            long initialMillis,
            Callback cb
    ) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(initialMillis);

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int mm = cal.get(Calendar.MINUTE);

        DatePickerDialog dp = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    // keep date, then ask time
                    TimePickerDialog tp = new TimePickerDialog(
                            context,
                            (tpView, hourOfDay, minute) -> {
                                Calendar out = Calendar.getInstance(tz);
                                out.set(Calendar.YEAR, year);
                                out.set(Calendar.MONTH, month);
                                out.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                out.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                out.set(Calendar.MINUTE, minute);
                                out.set(Calendar.SECOND, 0);
                                out.set(Calendar.MILLISECOND, 0);
                                if (cb != null) cb.onPicked(out.getTimeInMillis());
                            },
                            hh,
                            mm,
                            true
                    );
                    tp.show();
                },
                y, m, d
        );
        dp.show();
    }

    /**
     * AllDay用：日付だけ選んで 00:00:00 を返す
     */
    public static void pickDateStartAllDay(
            Context context,
            long initialMillis,
            Callback cb
    ) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(initialMillis);

        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar out = Calendar.getInstance(tz);
                    out.set(Calendar.YEAR, year);
                    out.set(Calendar.MONTH, month);
                    out.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    out.set(Calendar.HOUR_OF_DAY, 0);
                    out.set(Calendar.MINUTE, 0);
                    out.set(Calendar.SECOND, 0);
                    out.set(Calendar.MILLISECOND, 0);
                    if (cb != null) cb.onPicked(out.getTimeInMillis());
                },
                y, m, d
        );
        dp.show();
    }
}