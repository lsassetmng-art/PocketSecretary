package com.lsam.pocketsecretary.history;

import android.content.Context;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationHistoryStore {

    private static volatile NotificationHistoryStore INSTANCE;

    private final AppDatabase db;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    private NotificationHistoryStore(Context context) {
        db = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "pocketsecretary.db"
        )
        // 履歴は小規模・READ ONLY 想定。UIでの表示を簡単にするため許可。
        .allowMainThreadQueries()
        .build();
    }

    public static NotificationHistoryStore get(Context context) {
        if (INSTANCE == null) {
            synchronized (NotificationHistoryStore.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotificationHistoryStore(context);
                }
            }
        }
        return INSTANCE;
    }

    public void appendAsync(String source, String title, String text) {
        long now = System.currentTimeMillis();
        NotificationHistoryEntity e = new NotificationHistoryEntity(now, source, title, text);
        io.execute(() -> db.historyDao().insert(e));
    }

    public List<NotificationHistoryEntity> latestBlocking(int limit) {
        return db.historyDao().latest(limit);
    }
}