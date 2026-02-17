package com.lsam.pocketsecretary.data.event;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database for local events.
 */
@Database(
        entities = {EventEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class EventDatabase extends RoomDatabase {

    private static volatile EventDatabase INSTANCE;

    public abstract EventDao eventDao();

    public static EventDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (EventDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    EventDatabase.class,
                                    "ps_events.db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}