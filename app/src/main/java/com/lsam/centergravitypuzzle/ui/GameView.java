package com.lsam.centergravitypuzzle.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.lsam.centergravitypuzzle.core.Board;

public class GameView extends View {

    private final Board board;
    private final Paint paint = new Paint();

    public GameView(Context context, Board board) {
        super(context);
        this.board = board;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cell = getWidth() / Board.SIZE;

        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                int v = board.get(x, y);
                if (v == Board.EMPTY) continue;

                paint.setColor(0xff000000 | (v * 0x00202020));
                canvas.drawRect(
                    x * cell,
                    y * cell,
                    (x + 1) * cell,
                    (y + 1) * cell,
                    paint
                );
            }
        }
    }
}
