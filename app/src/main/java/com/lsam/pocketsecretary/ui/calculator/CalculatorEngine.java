package com.lsam.pocketsecretary.ui.calculator;

import java.util.Stack;

public final class CalculatorEngine {

    private CalculatorEngine() {}

    public static double evaluate(String expression) {
        if (expression == null || expression.isEmpty()) return 0.0;
        return compute(expression);
    }

    private static double compute(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> ops = new Stack<>();

        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() &&
                        (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if (isOperator(c)) {
                while (!ops.isEmpty() &&
                        precedence(ops.peek()) >= precedence(c)) {
                    numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
                }
                ops.push(c);
            }
            i++;
        }

        while (!ops.isEmpty()) {
            numbers.push(applyOp(ops.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b == 0 ? 0 : a / b;
        }
        return 0;
    }
}
