package com.sjoerdhemminga.adventofcode2022.day08;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        doFile("input-test.txt");
        doFile("input.txt");
    }

    private static void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toList()
                    .toArray(char[][]::new);

            final boolean[][] visible = new boolean[grid.length][grid[0].length];

            markEdges(visible);
            traceRays(grid, visible);
            final int visibleCount = countVisibles(visible);

            dumpVisibles(visible);

            System.out.println("visibleCount = " + visibleCount);
        }

        System.out.println();
    }

    private static void dumpVisibles(final boolean[][] visible) {
        for (final boolean[] row : visible) {
            for (final boolean cell : row)
                System.out.print(cell ? 'X' : '.');
            System.out.println();
        }
    }

    private static int countVisibles(final boolean[][] visible) {
        int count = 0;

        for (final boolean[] row : visible)
            for (final boolean cell : row)
                if (cell) count++;

        return count;
    }

    private static void traceRays(final char[][] grid, final boolean[][] visible) {
        for (int i = 0; i < grid.length; i++) {
            int maxHeight = grid[i][0];
            for (int j = 1; j < grid[i].length; j++)
                if (maxHeight < grid[i][j]) {
                    visible[i][j] = true;
                    maxHeight = grid[i][j];
                }

            maxHeight = grid[i][grid[i].length - 1];
            for (int j = grid[i].length - 2; j > 0; j--)
                if (maxHeight < grid[i][j]) {
                    visible[i][j] = true;
                    maxHeight = grid[i][j];
                }
        }

        for (int j = 0; j < grid[0].length; j++) {
            int maxHeight = grid[0][j];
            for (int i = 1; i < grid.length; i++)
                if (maxHeight < grid[i][j]) {
                    visible[i][j] = true;
                    maxHeight = grid[i][j];
                }

            maxHeight = grid[grid.length - 1][j];
            for (int i = grid.length - 2; i > 0; i--)
                if (maxHeight < grid[i][j]) {
                    visible[i][j] = true;
                    maxHeight = grid[i][j];
                }
        }
    }

    private static void markEdges(final boolean[][] visible) {
        for (int i = 0; i < visible.length; i++) {
            visible[i][0] = true;
            visible[i][visible[i].length - 1] = true;
        }

        for (int j = 0; j < visible.length; j++) {
            visible[0][j] = true;
            visible[visible.length - 1][j] = true;
        }
    }
}
