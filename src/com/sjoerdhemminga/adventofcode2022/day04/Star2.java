package com.sjoerdhemminga.adventofcode2022.day04;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final long count = lines.filter(not(String::isBlank))
                    .map(Star2::parse)
                    .filter(Star2::overlap)
                    .peek(ints -> System.out.println(Arrays.toString(ints)))
                    .count();

            System.out.println("count = " + count);
        }
    }

    private static boolean overlap(final int[] sections) {
        return (sections[2] <= sections[0] && sections[0] <= sections[3]) ||
                (sections[2] <= sections[1] && sections[1] <= sections[3]) ||
                (sections[0] <= sections[2] && sections[2] <= sections[1]) ||
                (sections[0] <= sections[3] && sections[3] <= sections[1]);
    }

    private static int[] parse(final String line) {
        final int idx1 = line.indexOf('-');
        final int idx2 = line.indexOf(',', idx1 + 1);
        final int idx3 = line.indexOf('-', idx2 + 1);

        return new int[] {
            parseInt(line.substring(0, idx1)),
            parseInt(line.substring(idx1 + 1, idx2)),
            parseInt(line.substring(idx2 + 1, idx3)),
            parseInt(line.substring(idx3 + 1))
        };
    }
}
