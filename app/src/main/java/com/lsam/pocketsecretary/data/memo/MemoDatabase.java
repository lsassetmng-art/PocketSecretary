package com.lsam.pocketsecretary.data.memo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MemoEntity.class}, version = 1, exportSchema = false)
public abstract class MemoDatabase extends RoomDatabase {
    public abstract MemoDao memoDao();
}
