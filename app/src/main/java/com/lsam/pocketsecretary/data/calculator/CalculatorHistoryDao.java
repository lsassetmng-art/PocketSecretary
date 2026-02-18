package com.lsam.pocketsecretary.data.calculator;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalculatorHistoryDao {

    @Insert
    void insert(CalculatorHistoryEntity entity);

    @Query("SELECT * FROM calculator_history ORDER BY createdAt DESC LIMIT 5")
    List<CalculatorHistoryEntity> getLatestFive();

    @Query("DELETE FROM calculator_history WHERE id NOT IN (SELECT id FROM calculator_history ORDER BY createdAt DESC LIMIT 5)")
    void trimToFive();
}
