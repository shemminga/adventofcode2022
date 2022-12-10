package com.sjoerdhemminga.adventofcode2022.day10;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star1 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1().doFile("input-test-short.txt");
        new Star1().doFile("input-test.txt");
        new Star1().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[] registerValueChanges = lines.filter(not(String::isBlank))
                    .flatMapToInt(this::parse)
                    .toArray();

            final int[] registerValues = new int[registerValueChanges.length + 1];
            registerValues[0] = 1;
            for (int i = 0; i < registerValueChanges.length; i++)
                registerValues[i + 1] = registerValues[i] + registerValueChanges[i];

            System.out.println("registerValues = " + Arrays.toString(registerValues));

            if (registerValues.length > 10) { // Not test data
                final int sum = IntStream.of(20, 60, 100, 140, 180, 220)
                        .map(i -> registerValues[i - 1] * i)
                        .sum();

                System.out.println("sum = " + sum);
            }
        }

        System.out.println();
    }

    private IntStream parse(final String instr) {
        return switch (instr.charAt(0)) {
            case 'n' -> IntStream.of(0);
            case 'a' -> IntStream.of(0, parseInt(instr.substring(5)));
            default -> throw new AssertionError(instr);
        };
    }
}
