package com.lsam.pocketsecretary.data.event_ui;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.lsam.pocketsecretary.data.event.EventEntity;

@Database(entities = {EventEntity.class}, version = 1, exportSchema = false)
public abstract class EventUiDatabase extends RoomDatabase {
    public abstract EventUiDao eventUiDao();
}
