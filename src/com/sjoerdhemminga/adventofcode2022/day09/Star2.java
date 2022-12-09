package com.sjoerdhemminga.adventofcode2022.day09;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.util.function.Predicate.not;

public final class Star2 {
    private final int[][] knots = new int[10][2];
    private final List<int[]> visiteds = new ArrayList<>();

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2().doFile("input-test.txt");
        new Star2().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .map(l -> new int[]{l.charAt(0), parseInt(l.substring(2))})
                    .forEachOrdered(this::handle);

            final long countUniquePositions = visiteds.stream()
                    .map(coords -> coords[0] + "," + coords[1])
                    .distinct()
                    .count();

            System.out.println("countUniquePositions = " + countUniquePositions);
        }

        System.out.println();
    }

    private void handle(final int[] move) {
        final int[] transform = switch (move[0]) {
            case 'R' -> new int[]{0, 1};
            case 'U' -> new int[]{-1, 0};
            case 'L' -> new int[]{0, -1};
            case 'D' -> new int[]{1, 0};
            default -> throw new IllegalStateException("Unexpected value: " + move[0]);
        };

        //System.out.printf("=== === Move: %c %d State: head %3d,%3d tail %3d,%3d%n", move[0], move[1],
        //        knots[0][0], knots[0][1], knots[9][0], knots[9][1]);

        for (int i = 0; i < move[1]; i++) {
            doMove(transform);
            //System.out.printf("head %3d,%3d  tail %3d,%3d dist %d%n", head[0], head[1], tail[0], tail[1], distance());
            visiteds.add(Arrays.copyOf(knots[9], knots[9].length));
        }
    }

    private void doMove(final int[] transform) {
        knots[0][0] += transform[0];
        knots[0][1] += transform[1];

        for (int i = 1; i < knots.length; i++)
            follow(knots[i - 1], knots[i]);
    }

    private static void follow(final int[] head, final int[] tail) {
        final int di = abs(head[0] - tail[0]);
        final int dj = abs(head[1] - tail[1]);

        if (di <= 1 && dj <= 1) return; // Not enough tension

        if (di > 2 || dj > 2) throw new AssertionError("di = " + di + " dj = " + dj);

        if (di == 1) tail[0] = head[0];
        else if (di == 2) {
            final int delta = tail[0] - head[0];
            final int dir = delta / abs(delta);
            tail[0] = head[0] + dir;
        }

        if (dj == 1) tail[1] = head[1];
        else if (dj == 2) {
            final int delta = tail[1] - head[1];
            final int dir = delta / abs(delta);
            tail[1] = head[1] + dir;
        }
    }

    private static int distance(final int[] head, final int[] tail) {
        final int di = abs(head[0] - tail[0]);
        final int dj = abs(head[1] - tail[1]);

        if (di > 1 || dj > 1) throw new AssertionError("di = " + di + " dj = " + dj);

        return max(di, dj);
    }
}
