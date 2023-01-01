package com.sjoerdhemminga.adventofcode2022.infi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;

public final class Part1 {
    private final String filename;

    private final int[] coords = {0, 0};
    private int[] course = {0, -1};

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Part1("input-test.txt").doFile();
        new Part1("input.txt").doFile();
    }

    private Part1(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Part1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(s -> s.split(" "))
                    .forEachOrdered(this::move);

            System.out.println("Arrays.toString(coords) = " + Arrays.toString(coords));
            System.out.println("Arrays.toString(course) = " + Arrays.toString(course));

            final int manhattanDistanceFromOrigin = abs(coords[0]) + abs(coords[1]);
            System.out.println("manhattanDistanceFromOrigin = " + manhattanDistanceFromOrigin);
        }

        System.out.println();
    }

    private void move(final String[] instr) {
        final int val = parseInt(instr[1]);
        switch (instr[0]) {
        case "draai" -> turn(val);
        case "loop", "spring" -> {
            coords[0] += val * course[0];
            coords[1] += val * course[1];
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
