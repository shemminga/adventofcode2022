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
import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2().doFile("input-test-short.txt");
        new Star2().doFile("input-test.txt");
        new Star2().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

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
                drawCrt(registerValues);
            }
        }

        System.out.println();
    }

    private void drawCrt(final int[] registerValues) {
        for (int line = 0; line < 6; line++) {
            for (int pixel = 0; pixel < 40; pixel++) {
                final int cycle = line * 40 + pixel + 1;
                if (abs(registerValues[cycle - 1] - pixel) <= 1) System.out.print('#');
                else System.out.print('.');
            }
            System.out.println();
        }
    }

    private IntStream parse(final String instr) {
        return switch (instr.charAt(0)) {
            case 'n' -> IntStream.of(0);
            case 'a' -> IntStream.of(0, parseInt(instr.substring(5)));
            default -> throw new AssertionError(instr);
        };
    }
}
