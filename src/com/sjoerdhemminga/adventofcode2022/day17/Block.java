package com.sjoerdhemminga.adventofcode2022.day17;

import java.util.List;

final class Block {
    private final char[][] block = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    private int bottomRow;
    private int leftCol = 2;

    Block(BlockInitializer initializer, int level) {
        initializer.apply(block);
        bottomRow = level;
    }

    void moveHorizontal(final List<char[]> chamber, final char jet) {
        if (jet == '>') moveRight(chamber);
        else if (jet == '<') moveLeft(chamber);
        else throw new AssertionError(jet);
    }

    private void moveRight(final List<char[]> chamber) {
        int testRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                final char[] row = chamber.get(testRow);
                if (block[i][j] != 0 && ((leftCol + 1 + j) >= row.length || row[leftCol + 1 + j] != 0)) return;
            }

            testRow++;
        }

        leftCol++;
    }

    private void moveLeft(final List<char[]> chamber) {
        if (leftCol <= 0) return;

        int testRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0 && chamber.get(testRow)[leftCol - 1 + j] != 0) return;
            }

            testRow++;
        }

        leftCol--;
    }

    boolean moveDown(final List<char[]> chamber) {
        if (bottomRow <= 0) return false;

        int testRow = bottomRow - 1;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0 && chamber.get(testRow)[leftCol + j] != 0) return false;
            }

            testRow++;
        }

        bottomRow--;
        return true;
    }

    void rest(final List<char[]> chamber) {
        int curRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0) chamber.get(curRow)[leftCol + j] = '#';
            }

            curRow++;
        }
    }

    int getBottomRow() {
        return bottomRow;
    }

    int getLeftCol() {
        return leftCol;
    }

    char[][] getBlock() {
        return block;
    }
}
