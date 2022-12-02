package com.sjoerdhemminga.adventofcode2022.day01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] ints = lines
                    .mapToInt(line -> line.isBlank() ? -1 : Integer.parseInt(line.strip()))
                    .toArray();

            int cur = 0;
            int max = 0;
            for (final int item : ints)
                if (item >= 0) cur += item;
                else { max = Math.max(max, cur); cur = 0; }

            max = Math.max(max, cur);

            System.out.println(max);
        }
    }
}
