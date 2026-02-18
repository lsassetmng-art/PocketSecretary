package com.lsam.pocketsecretary.data.event_ui;

import androidx.room.Dao;
import androidx.room.Query;

import com.lsam.pocketsecretary.data.event.EventEntity;

import java.util.List;

@Dao
public interface EventUiDao {

    @Query("SELECT * FROM events WHERE startAt >= :startMs AND startAt < :endMs ORDER BY " +
            "CASE WHEN allDay = 1 THEN 0 ELSE 1 END ASC, " +
            "startAt ASC")
    List<EventEntity> getEventsBetween(long startMs, long endMs);

    @Query("SELECT * FROM events WHERE startAt >= :startMs AND startAt < :endMs AND allDay = 1 ORDER BY startAt ASC")
    List<EventEntity> getAllDayBetween(long startMs, long endMs);
}
