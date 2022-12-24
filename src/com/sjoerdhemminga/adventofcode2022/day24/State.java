package com.sjoerdhemminga.adventofcode2022.day24;

import java.util.List;

record State(int i, int j, int steps, List<Blizzard> blizzards, Grid grid) {
    boolean isValid() {
        return blizzards.stream()
                .noneMatch(b -> b.i() == i && b.j() == j);
    }

    State next(final int newI, final int newJ) {
        final List<Blizzard> newBlizzards = blizzards.stream()
                .map(Blizzard::next)
                .toList();

        return new State(newI, newJ, steps + 1, newBlizzards, grid);
    }

    String steplessCode() {
        final StringBuilder sb = new StringBuilder();
        sb.append(i).append(';').append(j);

        for (final Blizzard blizzard : blizzards) blizzard.code(sb.append(';'));

        return sb.toString();
    }

    State withSteps(final int newSteps) {
        return new State(i, j, newSteps, blizzards, grid);
    }

    String dump() {
        final char[][] dumpGrid = grid.getDumpGrid();

        blizzards.forEach(b -> {
            final char curVal = dumpGrid[b.i()][b.j()];
            if (curVal == '.') dumpGrid[b.i()][b.j()] = b.dir().getCh();
            else if (curVal >= '0' && curVal <= '9') dumpGrid[b.i()][b.j()]++;
            else dumpGrid[b.i()][b.j()] = '2';
        });

        dumpGrid[i][j] = 'E';

        final StringBuilder sb = new StringBuilder();
        for (final char[] row : dumpGrid)
            sb.append(row).append('\n');

        return sb.toString();
    }
}
