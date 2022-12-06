package com.sjoerdhemminga.adventofcode2022.day06;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {

    public static final int MARKER_LENGTH = 14;

    public static void main(final String... args) throws IOException, URISyntaxException {
        doFile("input-test1.txt");
        doFile("input-test2.txt");
        doFile("input-test3.txt");
        doFile("input-test4.txt");
        doFile("input-test5.txt");
        doFile("input.txt");
    }

    private static void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("filename = " + filename);
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[] s = lines.filter(not(String::isBlank))
                    .findFirst()
                    .orElseThrow()
                    .toCharArray();

            final int processedCount = findSOPLastIdx(s) + 1;

            System.out.println("processedCount = " + processedCount);
        }

        System.out.println();
    }

    private static int findSOPLastIdx(final char[] s) {
        int i = MARKER_LENGTH;
        i: while (i < s.length) {
            for (int j = 1; j < MARKER_LENGTH; j++) {
                for (int k = 0; k < j; k++) {
                    //System.out.printf("i = %02d j = %02d k = %02d %n", i, j, k);
                    if (s[i - k] == s[i - j]) {
                        i += MARKER_LENGTH - j;
                        continue i;
                    }
                }
            }
            return i;
        }

        throw new AssertionError();
    }
}
