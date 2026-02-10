package com.lsam.pocketsecretary.core.repeat;

import java.util.Calendar;

public class RepeatUtil {
    // repeat: null | daily | weekly | weekday
    public static long nextAt(long baseAt, String repeat) {
        if (repeat == null) return baseAt;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(baseAt);
        switch (repeat) {
            case "daily":
                c.add(Calendar.DATE, 1); break;
            case "weekly":
                c.add(Calendar.DATE, 7); break;
            case "weekday":
                do { c.add(Calendar.DATE, 1); }
                while (c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY
                    || c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY);
                break;
            default:
                return baseAt;
        }
        return c.getTimeInMillis();
    }
}
