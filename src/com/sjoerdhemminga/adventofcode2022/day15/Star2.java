package com.sjoerdhemminga.adventofcode2022.day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

public final class Star2 {
    private static final Pattern PATTERN =
            Pattern.compile("^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$");
    private static final long I_MULTIPLY = 4_000_000;

    private final String filename;
    private final int maxIJ;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-test.txt", 20).doFile();
        new Star2("input.txt", 4_000_000).doFile();
    }

    private Star2(final String filename, final int maxIJ) {
        this.filename = filename;
        this.maxIJ = maxIJ;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[][] meas = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(m -> new String[]{m.group(1), m.group(2), m.group(3), m.group(4)})
                    .map(s -> new int[]{parseInt(s[0]), parseInt(s[1]), parseInt(s[2]), parseInt(s[3])})
                    .map(s -> new int[]{s[0], s[1], manhattan(s[0], s[1], s[2], s[3])})
                    .toList()
                    .toArray(int[][]::new);

            long tuningFreq = -1;
            i: for (int i = 0; i <= maxIJ; i++) {
                j: for (int j = 0; j <= maxIJ; j++) {
                    boolean covered = false;
                    m: for (final int[] m : meas) {
                        if (manhattan(m[0], m[1], i, j) <= m[2]) {
                            j = nextFreeJ(m, i) - 1; // Skip ahead a bit
                            covered = true;
                            break m;
                        }
                    }

                    if (!covered) {
                        tuningFreq = i * I_MULTIPLY + j;
                        break i;
                    }


                }

                if (i % 1000 == 0) System.out.println("i = " + i);
            }

            System.out.println("tuningFreq = " + tuningFreq);
        }

        System.out.println();
    }

    private int nextFreeJ(final int[] meas, final int i) {
        final int manhattan = meas[2];
        final int dist = abs(i - meas[0]);

        if (dist > manhattan) throw new AssertionError();
        final int dev = manhattan - dist;
        return meas[1] + dev + 1;
    }

    private int manhattan(final int i1, final int j1, final int i2, final int j2) {
        return abs(i1 - i2) + abs(j1 - j2);
    }
}
