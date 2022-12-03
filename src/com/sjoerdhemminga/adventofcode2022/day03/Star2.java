package com.sjoerdhemminga.adventofcode2022.day03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final String[] elfs = lines.filter(not(String::isBlank))
                    .toArray(String[]::new);

            int sum = 0;
            for (int i = 0; i < elfs.length; i += 3) {
                final int c = findIntersection(elfs[i], elfs[i + 1], elfs[i + 2]);
                sum += (c > 'a') ? (c - 'a' + 1) : (c - 'A' + 27);
            }

            System.out.println("sum = " + sum);
        }
    }

    private static int findIntersection(final String elf0, final String elf1, final String elf2) {
        return elf0.chars()
                .filter(c -> elf1.indexOf(c) >= 0)
                .filter(c -> elf2.indexOf(c) >= 0)
                .findFirst()
                .orElseThrow();
    }
}
