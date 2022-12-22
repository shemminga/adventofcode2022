package com.sjoerdhemminga.adventofcode2022.day22;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

record Move(int distance, int turnDirection) {
    static final int TD_LEFT = 'L';
    static final int TD_RIGHT = 'R';
    static final int TD_NULL = 0;

    static List<Move> parseMoves(final String str) {
        final List<Move> moves = new ArrayList<>();

        int curPos = 0;
        do {
            final int next = findTD(str, curPos);

            final int d = parseInt(str.substring(curPos, next));
            final char td = next == str.length() ? TD_NULL : str.charAt(next);
            moves.add(new Move(d, td));

            curPos = next + 1;
        } while (curPos < str.length());


        return moves;
    }

    private static int findTD(final String str, final int curPos) {
        final int nextL = str.indexOf(TD_LEFT, curPos);
        final int nextR = str.indexOf(TD_RIGHT, curPos);

        if (nextL < 0 && nextR < 0) return str.length();
        if (nextL < 0) return nextR;
        if (nextR < 0) return nextL;
        return min(nextL, nextR);
    }
}
