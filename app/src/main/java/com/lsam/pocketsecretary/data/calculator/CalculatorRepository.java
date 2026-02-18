package com.lsam.pocketsecretary.data.calculator;

import android.content.Context;
import androidx.room.Room;
import java.util.List;

public class CalculatorRepository {

    private final CalculatorHistoryDao dao;

    public CalculatorRepository(Context context) {
        CalculatorDatabase db = Room.databaseBuilder(
                context.getApplicationContext(),
                CalculatorDatabase.class,
                "ps_db"
        ).build();
        dao = db.calculatorHistoryDao();
    }

    public void save(String expression, String result) {
        CalculatorHistoryEntity e = new CalculatorHistoryEntity();
        e.expression = expression;
        e.result = result;
        e.createdAt = System.currentTimeMillis();
        dao.insert(e);
        dao.trimToFive();
    }

    public List<CalculatorHistoryEntity> getLatest() {
        return dao.getLatestFive();
    }
}
