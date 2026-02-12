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
        // 髯橸ｽｻ繝ｻ・･髮弱・・ｽ・ｴ驍ｵ・ｺ繝ｻ・ｯ髯昴・閾・・・ｦ闕ｵ證ｦ・ｽ・ｨ繝ｻ・｡驛｢譎｢・ｽ・ｻREAD ONLY 髫ｲ・ｰ繝ｻ・ｳ髯橸ｽｳ陞｢・ｹ・つ繝ｻ陷るし・ｺ繝ｻ・ｧ驍ｵ・ｺ繝ｻ・ｮ鬮ｯ・ｦ繝ｻ・ｨ鬩穂ｼ夲ｽｽ・ｺ驛｢・ｧ陜｣・､繝ｻ・ｰ繝ｻ・｡髯ｷ髮・・･遶企豪・ｸ・ｺ陷ｷ・ｶ繝ｻ迢暦ｽｸ・ｺ雋・∞・ｽ繝ｻ蝮弱・・ｱ髯ｷ・ｿ繝ｻ・ｯ驍ｵ・ｲ郢晢ｽｻ        .allowMainThreadQueries()
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
