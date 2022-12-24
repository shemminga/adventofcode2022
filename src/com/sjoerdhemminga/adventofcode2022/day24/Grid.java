package com.sjoerdhemminga.adventofcode2022.day24;

import java.util.Arrays;

record Grid(int maxI, int maxJ, int entryJ, int exitJ) {
    char[][] getDumpGrid() {
        final char[][] dumpGrid = new char[maxI + 2][maxJ + 2];

        for (final char[] row : dumpGrid) {
            Arrays.fill(row, '.');
            row[0] = '#';
            row[maxJ + 1] = '#';
        }
        Arrays.fill(dumpGrid[0], '#');
        Arrays.fill(dumpGrid[maxI + 1], '#');

        dumpGrid[0][entryJ] = '.';
        dumpGrid[maxI + 1][exitJ] = '.';

        return dumpGrid;
    }
}
