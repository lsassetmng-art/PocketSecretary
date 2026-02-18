package com.lsam.pocketsecretary.data.event_ui;

import android.content.Context;

import com.lsam.pocketsecretary.data.event.EventDatabase;
import com.lsam.pocketsecretary.data.event.EventEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventUiRepository {

    public interface Callback<T> {
        void onSuccess(T value);
        void onError(Exception e);
    }

    private final EventDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EventUiRepository(Context context) {
        db = EventDatabase.get(context.getApplicationContext());
    }

    public void getEventsForDay(long dayStartMs,
                                long dayEndMs,
                                Callback<List<EventEntity>> cb) {

        executor.execute(() -> {
            try {
                List<EventEntity> list =
                        db.eventDao().listBetween(dayStartMs, dayEndMs);
                cb.onSuccess(list);
            } catch (Exception e) {
                cb.onError(e);
            }
        });
    }
}
