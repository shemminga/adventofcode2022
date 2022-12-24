package com.sjoerdhemminga.adventofcode2022.day24;

record Blizzard(int i, int j, Direction dir, Grid grid) {
    private Blizzard withI(final int newI) {
        return new Blizzard(newI, j, dir, grid);
    }

    private Blizzard withJ(final int newJ) {
        return new Blizzard(i, newJ, dir, grid);
    }

    Blizzard next() {
        return switch (dir) {
            case UP -> withI(i == 1 ? grid.maxI() : i - 1);
            case DOWN -> withI(i == grid.maxI() ? 1 : i + 1);
            case LEFT -> withJ(j == 1 ? grid.maxJ() : j - 1);
            case RIGHT -> withJ(j == grid.maxJ() ? 1 : j + 1);
        };
    }

    void code(final StringBuilder sb) {
        sb.append(i).append(dir.getCh()).append(j);
    }
}
