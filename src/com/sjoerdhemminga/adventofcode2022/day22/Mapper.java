package com.sjoerdhemminga.adventofcode2022.day22;

import java.util.HashMap;
import java.util.Map;

import static com.sjoerdhemminga.adventofcode2022.day22.IjFacing.fromCoordAndFacing;
import static com.sjoerdhemminga.adventofcode2022.day22.Star2.FACE_D;
import static com.sjoerdhemminga.adventofcode2022.day22.Star2.FACE_L;
import static com.sjoerdhemminga.adventofcode2022.day22.Star2.FACE_R;
import static com.sjoerdhemminga.adventofcode2022.day22.Star2.FACE_U;

final class Mapper {
    private final Map<IjFacing, IjFacing> map = new HashMap<>();
    private int edgeSize = -1;

    Mapper(final char[][] grid) {
        if (grid.length == 12) initForTest();
        else if (grid.length == 200) initForReal();
        else throw new AssertionError(grid.length);

        verifyMap(grid);
    }

    private void verifyMap(final char[][] grid) {
        final int nrOfOpenSides = 14;

        for (final IjFacing fromIjf : map.keySet()) {
            final int[] from = fromIjf.coord();
            if (from[0] >= 0 && from[0] < grid.length &&
                    from[1] >= 0 && from[1] < grid[from[0]].length &&
                    safeGet(grid, from) != ' ')
                throw new AssertionError(safeGet(grid, from) + " on " + fromIjf);

            final int[] to = mapCoord(from, fromIjf.facing());
            if (to[0] < 0 || to[0] >= grid.length || to[1] < 0 || to[1] >= grid[to[0]].length)
                throw new AssertionError("Out of range: " + fromCoordAndFacing(to, -1));
            if (safeGet(grid, to) == ' ')
                throw new AssertionError("Drop into the void: " + fromCoordAndFacing(to, -1));
        }

        if (map.size() != edgeSize * nrOfOpenSides)
            throw new AssertionError(map.size() + " != " + (edgeSize * nrOfOpenSides));
    }

    private static char safeGet(final char[][] grid, final int[] coord) {
        if (coord[0] < 0 || coord[0] >= grid.length || coord[1] < 0 || coord[1] >= grid[coord[0]].length)
            return '~';

        return grid[coord[0]][coord[1]];
    }

    private void initForTest() {
        edgeSize = 4;

        // Edges inclusive: 0-3 4-7 8-11 12-15

        for (int j1 = 8, j2 = 3; j1 <= 11 && j2 >= 0; j1++, j2--)
            map(-1, j1, FACE_U, 3, j2, FACE_D);

        for (int i1 = 0, j2 = 4; i1 <= 3 && j2 <= 7; i1++, j2++)
            map(i1, 7, FACE_L, 3, j2, FACE_D);

        for (int i1 = 0, i2 = 11; i1 <= 3 && i2 >= 8; i1++, i2--)
            map(i1, 12, FACE_R, i2, 16, FACE_L);

        for (int i1 = 4, j2 = 15; i1 <= 7 && j2 >= 12; i1++, j2--)
            map(i1, 12, FACE_R, 7, j2, FACE_D);

        for (int i1 = 4, j2 = 15; i1 <= 7 && j2 >= 12; i1++, j2--)
            map(i1, -1, FACE_L, 12, j2, FACE_U);

        for (int j1 = 0, j2 = 11; j1 <= 3 && j2 >= 8; j1++, j2--)
            map(8, j1, FACE_D, 12, j2, FACE_U);

        for (int j1 = 4, i2 = 11; j1 <= 7 && i2 >= 8; j1++, i2--)
            map(8, j1, FACE_D, i2, 7, FACE_R);
    }

    private void initForReal() {
        edgeSize = 50;

        // Edges inclusive: 0-49 50-99 100-149 150-199

        for (int i1 = 0, i2 = 149; i1 <= 49 && i2 >= 100; i1++, i2--)
            map(i1, 49, FACE_L, i2, -1, FACE_R);

        for (int i1 = 0, i2 = 149; i1 <= 49 && i2 >= 100; i1++, i2--)
            map(i1, 150, FACE_R, i2, 100, FACE_L);

        for (int j1 = 100, j2 = 0; j1 <= 149 && j2 <= 49; j1++, j2++)
            map(-1, j1, FACE_U, 200, j2, FACE_U);

        for (int j1 = 50, i2 = 150; j1 <= 99 && i2 <= 199; j1++, i2++)
            map(-1, j1, FACE_U, i2, -1, FACE_R);

        for (int j1 = 100, i2 = 50; j1 <= 149 && i2 <= 99; j1++, i2++)
            map(50, j1, FACE_D, i2, 100, FACE_L);

        for (int j1 = 0, i2 = 50; j1 <= 49 && i2 <= 99; j1++, i2++)
            map(99, j1, FACE_U, i2, 49, FACE_R);

        for (int j1 = 50, i2 = 150; j1 <= 99 && i2 <= 199; j1++, i2++)
            map(150, j1, FACE_D, i2, 50, FACE_L);
    }

    private void map(final int i1, final int j1, final int f1, final int i2, final int j2, final int f2) {
        map.put(
                new IjFacing(i1, j1, f1),
                new IjFacing(i2, j2, f2)
        );
        map.put(
                new IjFacing(i2, j2, reverseFacing(f2)),
                new IjFacing(i1, j1, reverseFacing(f1))
        );
    }

    private int reverseFacing(final int facing) {
        return switch (facing) {
            case FACE_R -> FACE_L;
            case FACE_D -> FACE_U;
            case FACE_L -> FACE_R;
            case FACE_U -> FACE_D;
            default -> throw new AssertionError(facing);
        };
    }

    int[] mapCoord(final int[] curCoord, final int facing) {
        final IjFacing fromIjf = fromCoordAndFacing(curCoord, facing);
        final IjFacing toIjf = map.get(fromIjf);

        //System.out.println(fromIjf + " -> " + toIjf);

        final int[] coord = toIjf.coord();

        switch (toIjf.facing()) {
        case FACE_R -> coord[1]++;
        case FACE_L -> coord[1]--;
        case FACE_U -> coord[0]--;
        case FACE_D -> coord[0]++;
        default -> throw new AssertionError(toIjf.facing());
        }

        return coord;
    }

    int mapFacing(final int[] curCoord, final int facing) {
        return map.get(fromCoordAndFacing(curCoord, facing)).facing();
    }
}
