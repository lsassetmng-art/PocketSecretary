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
        // 陞ｻ・･雎・ｽｴ邵ｺ・ｯ陝・臆・ｦ荵暦ｽｨ・｡郢晢ｽｻREAD ONLY 隲・ｳ陞ｳ螢ｹﾂ・蜂邵ｺ・ｧ邵ｺ・ｮ髯ｦ・ｨ驕会ｽｺ郢ｧ蝣､・ｰ・｡陷雁･竊鍋ｸｺ蜷ｶ・狗ｸｺ貅假ｽ・坎・ｱ陷ｿ・ｯ邵ｲ繝ｻ        .allowMainThreadQueries()
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
