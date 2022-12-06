package com.sjoerdhemminga.adventofcode2022.day06;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
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
        final URL input = Star1.class.getResource(filename);

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
        int i = 3;
        while (i < s.length) {
            if (s[i] == s[i - 1]) i += 3;
            else if (s[i] == s[i - 2] || s[i - 1] == s[i - 2]) i += 2;
            else if (s[i] == s[i - 3] || s[i - 1] == s[i - 3] || s[i - 2] == s[i - 3]) i++;
            else return i;
        }

        throw new AssertionError();
    }
}
