package com.sjoerdhemminga.adventofcode2022.day18;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star2 {
    private final String filename;
    private int indentLevel = 0;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-short.txt").doFile();
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
            final List<int[]> coords = lines.filter(not(String::isBlank))
                    .map(l -> l.split(","))
                    .map(c -> new int[]{parseInt(c[0]), parseInt(c[1]), parseInt(c[2])})
                    .toList();

            final int maxI = coords.stream()
                    .mapToInt(c -> c[0])
                    .max()
                    .orElseThrow();
            final int maxJ = coords.stream()
                    .mapToInt(c -> c[1])
                    .max()
                    .orElseThrow();
            final int maxK = coords.stream()
                    .mapToInt(c -> c[2])
                    .max()
                    .orElseThrow();

            final boolean[][][] space = new boolean[maxI + 1][maxJ + 1][maxK + 1];

            coords.forEach(c -> space[c[0]][c[1]][c[2]] = true);

            final boolean[][][] reachable = new boolean[maxI + 1][maxJ + 1][maxK + 1];
            floodReachable(reachable, space);

            int openSides = 0;
            for (int i = 0; i < space.length; i++) {
                for (int j = 0; j < space[i].length; j++) {
                    for (int k = 0; k < space[i][j].length; k++) {
                        if (space[i][j][k]) {
                            if (i == 0 || reachable[i - 1][j][k]) openSides++;
                            if (j == 0 || reachable[i][j - 1][k]) openSides++;
                            if (k == 0 || reachable[i][j][k - 1]) openSides++;

                            if (i == maxI || reachable[i + 1][j][k]) openSides++;
                            if (j == maxJ || reachable[i][j + 1][k]) openSides++;
                            if (k == maxK || reachable[i][j][k + 1]) openSides++;
                        }
                    }
                }
            }

            System.out.println("openSides = " + openSides);
        }

        System.out.println();
    }

    private void floodReachable(final boolean[][][] reachable, final boolean[][][] space) {
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                fillFrom(reachable, space, i, j, 0);
                fillFrom(reachable, space, i, j, space[i][j].length - 1);
            }
        }

        for (int i = 0; i < space.length; i++) {
            for (int k = 0; k < space[i][0].length; k++) {
                fillFrom(reachable, space, i, 0, k);
                fillFrom(reachable, space, i, space[i].length - 1, k);
            }
        }

        for (int j = 0; j < space[0].length; j++) {
            for (int k = 0; k < space[0][j].length; k++) {
                fillFrom(reachable, space, 0, j, k);
                fillFrom(reachable, space, space.length - 1, j, k);
            }
        }
    }

    private void fillFrom(final boolean[][][] reachable, final boolean[][][] space, final int i, final int j,
            final int k) {
        if (space[i][j][k]) return;
        if (reachable[i][j][k]) return; // Visited already

        reachable[i][j][k] = true;

        if (i > 0) fillFrom(reachable, space, i - 1, j, k);
        if (j > 0) fillFrom(reachable, space, i, j - 1, k);
        if (k > 0) fillFrom(reachable, space, i, j, k - 1);

        if (i < space.length - 1) fillFrom(reachable, space, i + 1, j, k);
        if (j < space[i].length - 1) fillFrom(reachable, space, i, j + 1, k);
        if (k < space[i][j].length - 1) fillFrom(reachable, space, i, j, k + 1);
    }
}
