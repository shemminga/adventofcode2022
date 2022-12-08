package com.sjoerdhemminga.adventofcode2022.day08;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        doFile("input-test.txt");
        doFile("input.txt");
    }

    private static void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toList()
                    .toArray(char[][]::new);

            int maxScore = 0;
            for (int i = 0; i < grid.length; i++)
                for (int j = 0; j < grid[i].length; j++)
                    maxScore = max(maxScore, calcScore(grid, i, j));

            System.out.println("maxScore = " + maxScore);
        }

        System.out.println();
    }

    private static int calcScore(final char[][] grid, final int i, final int j) {
        int scoreUp = 0;
        for (int ii = i - 1; ii >= 0; ii--) {
            scoreUp++;
            if (grid[ii][j] >= grid[i][j]) break;
        }

        int scoreDown = 0;
        for (int ii = i + 1; ii < grid.length; ii++) {
            scoreDown++;
            if (grid[ii][j] >= grid[i][j]) break;
        }

        int scoreLeft = 0;
        for (int jj = j - 1; jj >= 0; jj--) {
            scoreLeft++;
            if (grid[i][jj] >= grid[i][j]) break;
        }

        int scoreRight = 0;
        for (int jj = j + 1; jj < grid[i].length; jj++) {
            scoreRight++;
            if (grid[i][jj] >= grid[i][j]) break;
        }

        final int score = scoreUp * scoreDown * scoreLeft * scoreRight;

        //System.out.printf("i,j: %2d,%2d (%c): %2d (u) * %2d (l) * %2d (r) * %2d (d) = %3d%n", i, j, grid[i][j],
        //        scoreUp, scoreLeft, scoreRight, scoreDown, score);

        return score;
    }
}
