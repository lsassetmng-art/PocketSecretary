package com.lsam.pocketsecretary.data.event;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(EventEntity e);

    @Update
    void update(EventEntity e);

    @Delete
    void delete(EventEntity e);

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    EventEntity findById(String id);

    @Query("SELECT * FROM events ORDER BY startAt ASC")
    List<EventEntity> listAll();

    @Query("SELECT * FROM events WHERE startAt BETWEEN :fromMillis AND :toMillis ORDER BY startAt ASC")
    List<EventEntity> listBetween(long fromMillis, long toMillis);

    @Query("DELETE FROM events WHERE id = :id")
    void deleteById(String id);
}