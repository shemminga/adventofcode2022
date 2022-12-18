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

public final class Star1 {
    private final String filename;
    private int indentLevel = 0;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-short.txt").doFile();
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

            int openSides = 0;
            for (int i = 0; i < space.length; i++) {
                for (int j = 0; j < space[i].length; j++) {
                    for (int k = 0; k < space[i][j].length; k++) {
                        if (space[i][j][k]) {
                            if (i == 0 || !space[i - 1][j][k]) openSides++;
                            if (j == 0 || !space[i][j - 1][k]) openSides++;
                            if (k == 0 || !space[i][j][k - 1]) openSides++;

                            if (i == maxI || !space[i + 1][j][k]) openSides++;
                            if (j == maxJ || !space[i][j + 1][k]) openSides++;
                            if (k == maxK || !space[i][j][k + 1]) openSides++;
                        }
                    }
                }
            }

            System.out.println("openSides = " + openSides);
        }

        System.out.println();
    }
}
