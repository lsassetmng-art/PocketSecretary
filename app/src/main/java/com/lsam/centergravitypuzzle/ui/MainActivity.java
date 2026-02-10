package com.lsam.centergravitypuzzle.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import com.lsam.centergravitypuzzle.core.Board;
import com.lsam.centergravitypuzzle.core.Gravity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Board board = new Board(new Random(0), 5);
        Gravity.apply(board);

        setContentView(new GameView(this, board));
    }
}
