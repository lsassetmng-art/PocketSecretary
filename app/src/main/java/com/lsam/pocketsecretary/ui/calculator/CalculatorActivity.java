package com.lsam.pocketsecretary.ui.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.base.BaseActivity;
import com.lsam.pocketsecretary.core.theme.CalculatorTheme;
import com.lsam.pocketsecretary.core.theme.ThemeManager;

public class CalculatorActivity extends BaseActivity implements View.OnClickListener {

    private TextView expressionDisplay;
    private TextView resultDisplay;

    private StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        applyThemeToRoot(findViewById(android.R.id.content));

        expressionDisplay = findViewById(R.id.expressionDisplay);
        resultDisplay = findViewById(R.id.resultDisplay);

        initButtons();
        applyCalculatorTheme();
    }

    private void initButtons() {
        int[] ids = {
                R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
                R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,
                R.id.btnPlus,R.id.btnMinus,R.id.btnMultiply,R.id.btnDivide,
                R.id.btnDot,R.id.btnEqual,R.id.btnClear,R.id.btnBack
        };

        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnClear) {
            expression.setLength(0);
            updateDisplay();
            return;
        }

        if (id == R.id.btnBack) {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                updateDisplay();
            }
            return;
        }

        if (id == R.id.btnEqual) {
            try {
                double result = CalculatorEngine.evaluate(expression.toString());
                resultDisplay.setText(String.valueOf(result));
            } catch (Exception e) {
                resultDisplay.setText("Error");
            }
            return;
        }

        String input = ((Button) v).getText().toString();

        if (isOperator(input)) {
            if (expression.length() == 0) return;
            char last = expression.charAt(expression.length() - 1);
            if (isOperator(String.valueOf(last))) {
                expression.setCharAt(expression.length() - 1, input.charAt(0));
            } else {
                expression.append(input);
            }
        } else {
            expression.append(input);
        }

        updateDisplay();
    }

    private void updateDisplay() {
        expressionDisplay.setText(expression.toString());
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private void applyCalculatorTheme() {
        CalculatorTheme theme = ThemeManager.resolveCalculatorTheme(this);
        expressionDisplay.setTextColor(theme.base.textPrimary);
        resultDisplay.setTextColor(theme.accentColor);
    }
}
