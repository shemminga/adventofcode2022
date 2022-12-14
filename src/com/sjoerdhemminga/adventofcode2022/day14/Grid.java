package com.sjoerdhemminga.adventofcode2022.day14;

import java.util.stream.IntStream;

final class Grid {
    private final int[][] grid;

    Grid(final int sizeI, final int sizeJ) {
        grid = new int[sizeI][sizeJ];
    }

    Grid(final Ij maxIj) {
        this(maxIj.i() + 1, maxIj.j() + 1);
    }

    int get(final Ij ij) {
        return grid[ij.i()][ij.j()];
    }

    void set(final Ij ij, final int val) {
        grid[ij.i()][ij.j()] = val;
    }

    boolean inBounds(final Ij ij) {
        return ij.i() >= 0 && ij.i() < grid.length &&
                ij.j() >= 0 && ij.j() < grid[0].length;
    }

    String blockToString(final Ij start, final Ij end) {
        return start.streamToByRow(end)
                .flatMapToInt(ijStream -> IntStream.concat(ijStream.mapToInt(this::get), IntStream.of('\n')))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
