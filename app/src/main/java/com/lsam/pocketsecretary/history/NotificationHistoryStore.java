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
        // 螻･豁ｴ縺ｯ蟆剰ｦ乗ｨ｡繝ｻREAD ONLY 諠ｳ螳壹６I縺ｧ縺ｮ陦ｨ遉ｺ繧堤ｰ｡蜊倥↓縺吶ｋ縺溘ａ險ｱ蜿ｯ縲・        .allowMainThreadQueries()
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