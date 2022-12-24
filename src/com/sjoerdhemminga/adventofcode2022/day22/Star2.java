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

public final class Star2 {
    static final int FACE_R = 0;
    static final int FACE_D = 1;
    static final int FACE_L = 2;
    static final int FACE_U = 3;
    static final char[] FACING_CHARS = {'>', 'v', '<', '^'};

    private final String filename;
    private int facing;
    private int[] curCoord;
    private char[][] grid;
    private char[][] markGrid;

    private Mapper mapper;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-test.txt").doFile();
        new Star2("input.txt").doFile();
    }

    private Star2(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<String> strings = lines.filter(not(String::isBlank))
                    .toList();

            grid = strings.stream()
                    .limit(strings.size() - 1)
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
            makeItRectangular();
            final List<Move> moves = Move.parseMoves(strings.get(strings.size() - 1));

            markGrid = Arrays.stream(grid)
                    .map(row -> Arrays.copyOf(row, row.length))
                    .toArray(char[][]::new);

            curCoord = findStartCoord();
            facing = FACE_R;
            mark();

            mapper = new Mapper(grid);

            moves.forEach(this::doMove);

            dump();

            final int row = curCoord[0] + 1;
            final int col = curCoord[1] + 1;
            final int password = 1000 * row + 4 * col + facing;

            System.out.printf("row = %3d; col = %3d; facing = %d%n", row, col, facing);
            System.out.println("password = " + password);
        }

        System.out.println();
    }

    private void doMove(final Move move) {
        doMove(move.distance());

        facing = switch (move.turnDirection()) {
            case TD_LEFT -> facing == FACE_R ? FACE_U : facing - 1;
            case TD_RIGHT -> facing == FACE_U ? FACE_R : facing + 1;
            case TD_NULL -> facing;
            default -> throw new AssertionError(move.turnDirection());
        };

        mark();
        //dump();
    }

    private void doMove(final int steps) {
        for (int i = 0; i < steps; i++) {
            final int[] prevCoord = {curCoord[0], curCoord[1]};
            final int prevFacing = facing;
            moveOneStep();
            if (outOfRange() || curCell() == ' ') {
                //dump();
                final int[] cc = curCoord;
                curCoord = mapper.mapCoord(cc, facing);
                facing = mapper.mapFacing(cc, facing);
            }
            if (curCell() == '#') {
                // Hit the wall. UNDO UNDO UNDO
                curCoord = prevCoord;
                facing = prevFacing;
                return;
            }

            mark();
        }
    }

    private void moveOneStep() {
        switch (facing) {
        case FACE_R -> curCoord[1]++;
        case FACE_L -> curCoord[1]--;
        case FACE_U -> curCoord[0]--;
        case FACE_D -> curCoord[0]++;
        default -> throw new AssertionError(facing);
        }
    }

    private void mark() {
        markGrid[curCoord[0]][curCoord[1]] = FACING_CHARS[facing];
    }

    private void dump() {
        final StringBuilder sb = new StringBuilder();

        for (final char[] row : markGrid)
            sb.append("| ").append(row).append(" |\n");

        sb.append('\n');
        System.out.println(sb);
    }

    private boolean outOfRange() {
        return curCoord[0] < 0 || curCoord[0] >= grid.length || curCoord[1] < 0 || curCoord[1] >= grid[0].length;
    }

    private char curCell() {
        return grid[curCoord[0]][curCoord[1]];
    }

    private int[] findStartCoord() {
        for (int j = 0; j < grid[0].length; j++)
            if (grid[0][j] != ' ')
                return new int[]{0, j};
        throw new AssertionError();
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
