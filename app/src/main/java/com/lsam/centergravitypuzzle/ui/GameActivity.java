package com.lsam.centergravitypuzzle;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.centergravitypuzzle.core.Board;
import com.lsam.centergravitypuzzle.core.TurnEngine;
import com.lsam.centergravitypuzzle.ui.EffectOverlayView;
import com.lsam.centergravitypuzzle.ui.GameView;

public class GameActivity extends AppCompatActivity {

    private TurnEngine engine;
    private GameView gameView;
    private EffectOverlayView overlay;
    private TextView txtTurn;
    private TextView txtState;

    private AppSettings settings;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        settings = new AppSettings(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        engine = new TurnEngine(); // Phase4 APIJP_TEXT
        gameView = findViewById(R.id.gameView);
        overlay = findViewById(R.id.effectOverlay);
        txtTurn = findViewById(R.id.txtTurn);
        txtState = findViewById(R.id.txtState);

        Board board = engine.getBoard();
        gameView.bind(board, (tx, ty) -> {
            // --- Phase5：JP_TEXT（JP_TEXTGameViewJP_TEXTーJP_TEXTOK）
            overlay.onTap(tx, ty, board);

            // --- Core JP_TEXT（Phase4JP_TEXTAPI）
            int beforeTurn = engine.getTurn();
            engine.applyTurn(tx, ty);
            int afterTurn = engine.getTurn();

            // --- JP_TEXT：JP_TEXT/JP_TEXT/JP_TEXT「JP_TEXT」JP_TEXT（CoreJP_TEXT）
            overlay.onAfterApply(board);

            // --- JP_TEXT
            if (settings.isVibrationEnabled() && afterTurn != beforeTurn && vibrator != null) {
                try { vibrator.vibrate(12); } catch (Throwable ignored) {}
            }

            refreshHud();
            gameView.invalidate();
        });

        findViewById(R.id.btnReset).setOnClickListener(v -> {
            engine = new TurnEngine();
            gameView.bind(engine.getBoard(), (tx, ty) -> {
                overlay.onTap(tx, ty, engine.getBoard());
                int beforeTurn = engine.getTurn();
                engine.applyTurn(tx, ty);
                overlay.onAfterApply(engine.getBoard());
                if (settings.isVibrationEnabled() && engine.getTurn() != beforeTurn && vibrator != null) {
                    try { vibrator.vibrate(12); } catch (Throwable ignored) {}
                }
                refreshHud();
                gameView.invalidate();
            });
            overlay.reset();
            refreshHud();
            gameView.invalidate();
        });

        findViewById(R.id.btnBackTitle).setOnClickListener(v -> finish());

        refreshHud();
    }

    private void refreshHud() {
        txtTurn.setText("Turn: " + engine.getTurn());
        txtState.setText("State: " + String.valueOf(engine.getState()));
    }
}
