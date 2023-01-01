package com.sjoerdhemminga.adventofcode2022.infi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;

public final class Part2 {
    private final String filename;
    private final List<int[]> track = new ArrayList<>();

    private int[] coords = {0, 0};
    private int[] course = {0, -1};

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Part2("input-test.txt").doFile();
        new Part2("input.txt").doFile();
    }

    private Part2(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Part2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            track.add(coords);

            lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(s -> s.split(" "))
                    .forEachOrdered(this::move);

            printTrack();
        }

        System.out.println();
    }

    private void printTrack() {
        int minI = MAX_VALUE, maxI = MIN_VALUE, minJ = MAX_VALUE, maxJ = MIN_VALUE;

        for (final int[] c : track) {
            minI = min(minI, c[1]);
            maxI = max(maxI, c[1]);
            minJ = min(minJ, c[0]);
            maxJ = max(maxJ, c[0]);
        }

        final char[][] grid = new char[maxI - minI + 1][maxJ - minJ + 1];
        for (final char[] row : grid) Arrays.fill(row, '`');

        final int baseI = minI, baseJ = minJ; // Fucking stupid
        track.forEach(c -> grid[c[1] - baseI][c[0] - baseJ] = '#');

        final StringBuilder sb = new StringBuilder();
        for (final char[] row : grid) sb.append(row).append('\n');

        System.out.println(sb);
    }

    private void move(final String[] instr) {
        final int val = parseInt(instr[1]);
        switch (instr[0]) {
        case "draai" -> turn(val);
        case "loop" -> {
            for (int i = 0; i < val; i++) {
                coords = new int[]{coords[0] + course[0], coords[1] + course[1]};
                track.add(coords);
            }
        }
        case "spring" -> {
            coords = new int[]{coords[0] + val * course[0], coords[1] + val * course[1]};
            track.add(coords);
        }
        default -> throw new IllegalStateException("Unexpected value: " + instr[0]);
        }
    }

    private void turn(final int degree) {
        course = switch (degree) {
            case 90 -> new int[]{-course[1], course[0]};
            case 180, -180 -> new int[]{-course[0], -course[1]};
            case -90 -> new int[]{course[1], -course[0]};
            case 45 -> new int[]{cutToRange(course[0] - course[1]), cutToRange(course[0] + course[1])};
            case -45 -> new int[]{cutToRange(course[0] + course[1]), cutToRange(course[1] - course[0])};
            case 135 -> new int[]{cutToRange(-course[0] - course[1]), cutToRange(course[0] - course[1])};
            case -135 -> new int[]{cutToRange(course[1] - course[0]), cutToRange(-course[0] - course[1])};

            default -> throw new IllegalStateException("Unexpected value: " + degree);
        };
    }

    private int cutToRange(final int n) {
        return min(1, max(-1, n));
    }
}
