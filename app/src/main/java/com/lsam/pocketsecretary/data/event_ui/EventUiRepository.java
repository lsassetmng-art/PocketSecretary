package com.lsam.pocketsecretary.data.event_ui;

import android.content.Context;

import androidx.room.Room;

import com.lsam.pocketsecretary.data.event.EventEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventUiRepository {

    public interface Callback<T> {
        void onSuccess(T value);
        void onError(Exception e);
    }

    private final EventUiDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EventUiRepository(Context context) {
        EventUiDatabase db = Room.databaseBuilder(
                context.getApplicationContext(),
                EventUiDatabase.class,
                "ps_events_ui_db"
        ).build();
        dao = db.eventUiDao();
    }

    public void getEventsForDay(long dayStartMs, long dayEndMs, Callback<List<EventEntity>> cb) {
        executor.execute(() -> {
            try { cb.onSuccess(dao.getEventsBetween(dayStartMs, dayEndMs)); }
            catch (Exception e) { cb.onError(e); }
        });
    }
}
