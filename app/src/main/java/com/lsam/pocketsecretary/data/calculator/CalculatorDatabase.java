package com.lsam.pocketsecretary.data.calculator;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CalculatorHistoryEntity.class}, version = 1)
public abstract class CalculatorDatabase extends RoomDatabase {
    public abstract CalculatorHistoryDao calculatorHistoryDao();
}
