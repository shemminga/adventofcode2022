package com.sjoerdhemminga.adventofcode2022.day22;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2022.day22.Move.TD_LEFT;
import static com.sjoerdhemminga.adventofcode2022.day22.Move.TD_NULL;
import static com.sjoerdhemminga.adventofcode2022.day22.Move.TD_RIGHT;
import static java.util.function.Predicate.not;

public final class Star1 {
    private static final int FACE_R = 0;
    private static final int FACE_D = 1;
    private static final int FACE_L = 2;
    private static final int FACE_U = 3;

    private final String filename;
    private int facing;
    private int[] curCoord;
    private char[][] grid;
    private int[] firstsOnRows;
    private int[] lastsOnRows;
    private int[] firstsOnCols;
    private int[] lastsOnCols;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-test.txt").doFile();
        new Star1("input.txt").doFile();
    }

    private Star1(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<String> strings = lines.filter(not(String::isBlank))
                    .toList();

            grid = strings.stream()
                    .limit(strings.size() - 1)
                    .map(String::toCharArray)
                    .toList()
                    .toArray(char[][]::new);
            makeItRectangular();
            final List<Move> moves = Move.parseMoves(strings.get(strings.size() - 1));

            firstsOnRows = new int[grid.length];
            lastsOnRows = new int[grid.length];
            firstsOnCols = new int[grid[0].length];
            lastsOnCols = new int[grid[0].length];
            scanFirstsLasts();

            curCoord = new int[]{0, firstsOnRows[0]};
            facing = FACE_R;

            moves.forEach(this::doMove);

            final int row = curCoord[0] + 1;
            final int col = curCoord[1] + 1;
            final int password = 1000 * row + 4 * col + facing;

            System.out.printf("row = %3d; col = %3d; facing = %d%n", row, col, facing);
            System.out.println("password = " + password);
        }

        System.out.println();
    }

    private void doMove(final Move move) {
        switch (facing) {
        case FACE_R -> doMove(move.distance(), () -> curCoord[1]++, () -> curCoord[1] = firstsOnRows[curCoord[0]]);
        case FACE_L -> doMove(move.distance(), () -> curCoord[1]--, () -> curCoord[1] = lastsOnRows[curCoord[0]]);
        case FACE_U -> doMove(move.distance(), () -> curCoord[0]--, () -> curCoord[0] = lastsOnCols[curCoord[1]]);
        case FACE_D -> doMove(move.distance(), () -> curCoord[0]++, () -> curCoord[0] = firstsOnCols[curCoord[1]]);
        default -> throw new AssertionError(facing);
        };

        facing = switch (move.turnDirection()) {
            case TD_LEFT -> facing == FACE_R ? FACE_U : facing - 1;
            case TD_RIGHT -> facing == FACE_U ? FACE_R : facing + 1;
            case TD_NULL -> facing;
            default -> throw new AssertionError(move.turnDirection());
        };
    }

    private void doMove(final int steps, final Runnable mover, final Runnable wrapper) {
        for (int i = 0; i < steps; i++) {
            final int[] prevCoord = {curCoord[0], curCoord[1]};
            mover.run();
            if (outOfRange() || curCell() == ' ') wrapper.run();
            if (curCell() == '#') {
                // Hit the wall. UNDO UNDO UNDO
                curCoord = prevCoord;
                return;
            }
        }
    }

    private boolean outOfRange() {
        return curCoord[0] < 0 || curCoord[0] >= grid.length || curCoord[1] < 0 || curCoord[1] >= grid[0].length;
    }

    private char curCell() {
        return grid[curCoord[0]][curCoord[1]];
    }

    private void scanFirstsLasts() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != ' ') {
                    firstsOnRows[i] = j;
                    break;
                }
            }

            for (int j = grid[i].length - 1; j >= 0; j--) {
                if (grid[i][j] != ' ') {
                    lastsOnRows[i] = j;
                    break;
                }
            }
        }

        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j] != ' ') {
                    firstsOnCols[j] = i;
                    break;
                }
            }

            for (int i = grid.length - 1; i >= 0; i--) {
                if (grid[i][j] != ' ') {
                    lastsOnCols[j] = i;
                    break;
                }
            }
        }
    }

    private void makeItRectangular() {
        final int max = Arrays.stream(grid)
                .mapToInt(row -> row.length)
                .max()
                .orElseThrow();

        for (int i = 0; i < grid.length; i++) {
            if (grid[i].length < max) {
                final char[] newRow = new char[max];
                Arrays.fill(newRow, ' ');
                System.arraycopy(grid[i], 0, newRow, 0, grid[i].length);
                grid[i] = newRow;
            }

            if (grid[i].length != max) throw new AssertionError("Not rectangular?!");
        }
    }
}
