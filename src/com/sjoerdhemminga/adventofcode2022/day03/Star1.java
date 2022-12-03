package com.sjoerdhemminga.adventofcode2022.day03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int sum = lines.filter(not(String::isBlank))
                    .map(line -> new String[]{line.substring(0, line.length() / 2), line.substring(line.length() / 2)})
                    .map(compartments -> findIntersection(compartments[0], compartments[1]))
                    .mapToInt(c -> (c > 'a') ? (c - 'a' + 1) : (c - 'A' + 27))
                    .sum();

            System.out.println("sum = " + sum);
        }
    }

    private static int findIntersection(final String compartment0, final String compartment1) {
        return compartment0.chars()
                .filter(c -> compartment1.indexOf(c) >= 0)
                .findFirst()
                .orElseThrow();
    }
}
