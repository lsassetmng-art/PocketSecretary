package com.lsam.pocketsecretary.data.calculator;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calculator_history")
public class CalculatorHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String expression;
    public String result;
    public long createdAt;
}
