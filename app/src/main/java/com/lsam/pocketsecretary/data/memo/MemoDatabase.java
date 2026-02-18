package com.lsam.pocketsecretary.data.memo;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import android.content.Context;

@Database(entities = {MemoEntity.class}, version = 1, exportSchema = false)
public abstract class MemoDatabase extends RoomDatabase {
    public abstract MemoDao memoDao();

private static MemoDatabase instance;

public static synchronized MemoDatabase get(Context context) {
    if (instance == null) {
        instance = Room.databaseBuilder(
                context.getApplicationContext(),
                MemoDatabase.class,
                "memo_db"
        ).build();
    }
    return instance;
}

}
