package com.sjoerdhemminga.adventofcode2022.day01;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] ints = lines
                    .mapToInt(line -> line.isBlank() ? -1 : Integer.parseInt(line.strip()))
                    .toArray();

            int cur = 0;
            final List<Integer> elfs = new ArrayList<>();
            for (final int item : ints)
                if (item >= 0) cur += item;
                else { elfs.add(cur); cur = 0; }

            elfs.add(cur);

            System.out.println("elfs = " + elfs);

            final int sum = elfs.stream()
                    .sorted(Collections.reverseOrder())
                    .limit(3)
                    .mapToInt(x -> x)
                    .sum();

            System.out.println("sum = " + sum);
        }
    }
}
