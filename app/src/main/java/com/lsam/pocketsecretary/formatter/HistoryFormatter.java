package com.lsam.pocketsecretary.formatter;

import com.lsam.pocketsecretary.history.NotificationHistoryEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryFormatter {

    private static final int LIMIT = 200;

    public static String format(List<NotificationHistoryEntity> list, String emptyText) {

        if (list == null || list.isEmpty()) {
            return emptyText;
        }

        SimpleDateFormat fmt =
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        StringBuilder sb = new StringBuilder();

        for (NotificationHistoryEntity e : list) {
            sb.append(fmt.format(e.createdAtEpochMs))
              .append(" / ")
              .append(e.source)
              .append("\n")
              .append(e.title)
              .append("\n")
              .append(e.text)
              .append("\n\n");
        }

        return sb.toString();
    }

    public static int defaultLimit() {
        return LIMIT;
    }
}
