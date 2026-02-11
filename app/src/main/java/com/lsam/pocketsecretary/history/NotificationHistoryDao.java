package com.lsam.pocketsecretary.history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationHistoryDao {

    @Insert
    long insert(NotificationHistoryEntity entity);

    @Query("SELECT * FROM notification_history ORDER BY id DESC LIMIT :limit")
    List<NotificationHistoryEntity> latest(int limit);
}