package com.sjoerdhemminga.adventofcode2022.day17;

final class BlockStar2 {
    private final char[][] block = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    private long bottomRow;
    private int leftCol = 2;

    BlockStar2(final BlockInitializer initializer, final long level) {
        initializer.apply(block);
        bottomRow = level;
    }

    void moveHorizontal(final Chamber chamber, final char jet) {
        if (jet == '>') moveRight(chamber);
        else if (jet == '<') moveLeft(chamber);
        else throw new AssertionError(jet);
    }

    private void moveRight(final Chamber chamber) {
        long testRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                final char[] row = chamber.get(testRow);
                if (block[i][j] != 0 && ((leftCol + 1 + j) >= row.length || row[leftCol + 1 + j] != 0)) return;
            }

            testRow++;
        }

        leftCol++;
    }

    private void moveLeft(final Chamber chamber) {
        if (leftCol <= 0) return;

        long testRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0 && chamber.get(testRow)[leftCol - 1 + j] != 0) return;
            }

            testRow++;
        }

        leftCol--;
    }

    boolean moveDown(final Chamber chamber) {
        if (bottomRow <= chamber.bottom()) return false;

        long testRow = bottomRow - 1;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0 && chamber.get(testRow)[leftCol + j] != 0) return false;
            }

            testRow++;
        }

        bottomRow--;
        return true;
    }

    void rest(final Chamber chamber) {
        long curRow = bottomRow;

        for (int i = block.length - 1; i >= 0; i--) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] != 0) chamber.get(curRow)[leftCol + j] = '#';
            }

            curRow++;
        }

        chamber.discardUnreachableRows();
    }

    long getBottomRow() {
        return bottomRow;
    }

    int getLeftCol() {
        return leftCol;
    }

    char[][] getBlock() {
        return block;
    }
}
