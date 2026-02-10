package com.lsam.centergravitypuzzle.core;

import java.util.HashSet;
import java.util.Set;

public class MatchResolver {

    public static boolean resolve(Board board) {
        Set<String> remove = new HashSet<>();

        // JP_TEXT
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE - 2; x++) {
                int v = board.get(x, y);
                if (v != Board.EMPTY &&
                    v == board.get(x + 1, y) &&
                    v == board.get(x + 2, y)) {
                    remove.add(x + "," + y);
                    remove.add((x + 1) + "," + y);
                    remove.add((x + 2) + "," + y);
                }
            }
        }

        // JP_TEXT
        for (int x = 0; x < Board.SIZE; x++) {
            for (int y = 0; y < Board.SIZE - 2; y++) {
                int v = board.get(x, y);
                if (v != Board.EMPTY &&
                    v == board.get(x, y + 1) &&
                    v == board.get(x, y + 2)) {
                    remove.add(x + "," + y);
                    remove.add(x + "," + (y + 1));
                    remove.add(x + "," + (y + 2));
                }
            }
        }

        for (String key : remove) {
            String[] p = key.split(",");
            board.set(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Board.EMPTY);
        }

        return !remove.isEmpty();
    }
}
