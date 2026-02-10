package com.lsam.centergravitypuzzle.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.lsam.centergravitypuzzle.core.Board;

public class EffectOverlayView extends View {

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float dirAlpha = 0f;
    private float gravAlpha = 0f;
    private float matchAlpha = 0f;

    // JP_TEXT（JP_TEXT）
    private int lastTx = -1, lastTy = -1;

    // JP_TEXT
    private int boardW = 9, boardH = 9;

    public EffectOverlayView(Context c) { super(c); init(); }
    public EffectOverlayView(Context c, AttributeSet a) { super(c, a); init(); }
    public EffectOverlayView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6f);
    }

    public void reset() {
        dirAlpha = gravAlpha = matchAlpha = 0f;
        lastTx = lastTy = -1;
        invalidate();
    }

    public void onTap(int tx, int ty, Board board) {
        lastTx = tx; lastTy = ty;
        if (board != null) {
            boardW = board.getWidth();
            boardH = board.getHeight();
        }
        pulseDir();
    }

    public void onAfterApply(Board board) {
        if (board != null) {
            boardW = board.getWidth();
            boardH = board.getHeight();
        }
        pulseGravity();
        pulseMatch(); // “JP_TEXT”JP_TEXT（JP_TEXT）
    }

    private void pulseDir() {
        ValueAnimator a = ValueAnimator.ofFloat(1f, 0f);
        a.setDuration(180);
        a.addUpdateListener(v -> { dirAlpha = (float) v.getAnimatedValue(); invalidate(); });
        a.start();
    }

    private void pulseGravity() {
        ValueAnimator a = ValueAnimator.ofFloat(0.9f, 0f);
        a.setDuration(220);
        a.addUpdateListener(v -> { gravAlpha = (float) v.getAnimatedValue(); invalidate(); });
        a.start();
    }

    private void pulseMatch() {
        ValueAnimator a = ValueAnimator.ofFloat(0.7f, 0f);
        a.setDuration(140);
        a.addUpdateListener(v -> { matchAlpha = (float) v.getAnimatedValue(); invalidate(); });
        a.start();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        // JP_TEXT（JP_TEXT GameView JP_TEXT）
        float w = getWidth();
        float h = getHeight();

        // JP_TEXT（JP_TEXT）：JP_TEXT
        if (gravAlpha > 0f) {
            p.setAlpha((int)(gravAlpha * 200));
            float cx = w / 2f;
            float cy = h / 2f;
            c.drawLine(cx, 0, cx, h, p);
            c.drawLine(0, cy, w, cy, p);
        }

        // JP_TEXTーJP_TEXT：JP_TEXT（JP_TEXT）
        if (dirAlpha > 0f && lastTx >= 0 && lastTy >= 0) {
            p.setAlpha((int)(dirAlpha * 255));
            float cellW = w / boardW;
            float cellH = h / boardH;

            float x = (lastTx + 0.5f) * cellW;
            float y = (lastTy + 0.5f) * cellH;

            float cx = w / 2f;
            float cy = h / 2f;

            c.drawLine(x, y, cx, cy, p);
            // JP_TEXT（JP_TEXTVJP_TEXT）
            float hx = (x + cx) * 0.5f;
            float hy = (y + cy) * 0.5f;
            c.drawCircle(hx, hy, 16f, p);
        }

        // “JP_TEXT”JP_TEXT（JP_TEXT）
        if (matchAlpha > 0f) {
            p.setAlpha((int)(matchAlpha * 180));
            c.drawRect(10, 10, w - 10, h - 10, p);
        }
    }
}
