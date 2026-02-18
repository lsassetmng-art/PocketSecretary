package com.lsam.pocketsecretary.data.todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TodoEntity.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}
