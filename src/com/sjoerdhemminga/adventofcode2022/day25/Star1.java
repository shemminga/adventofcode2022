package com.sjoerdhemminga.adventofcode2022.day25;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private final String filename;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-self.txt").doFile();
        new Star1("input-test.txt").doFile();
        new Star1("input.txt").doFile();
    }

    private Star1(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final long sum = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .mapToLong(this::parseSnafu)
                    .sum();

            System.out.println("sum = " + sum);

            final String snafuSum = toSnafu(sum);
            System.out.println("snafuSum = " + snafuSum);
        }

        System.out.println();
    }

    private String toSnafu(final long num) {
        final StringBuilder sb = new StringBuilder();
        long cur = num;

        while (cur > 0) {
            final int mod5 = (int) (cur % 5);

            final char snafuDigit = switch (mod5) {
                case 3 -> { cur += 2; yield '='; }
                case 4 -> { cur += 1; yield '-'; }
                case 0, 1, 2 -> { cur -= mod5; yield (char) (mod5 + '0'); }
                default -> throw new AssertionError(mod5);
            };

            cur /= 5;

            sb.append(snafuDigit);
        }

        return sb.reverse().toString();
    }

    private long parseSnafu(final String snafu) {
        long pos = 1;
        long val = 0;

        for (int i = snafu.length() - 1; i >= 0; i--) {
            final char c = snafu.charAt(i);
            final int digit = switch (c) {
                case '=' -> -2;
                case '-' -> -1;
                case '0', '1', '2' -> c - '0';
                default -> throw new AssertionError(c);
            };

            val += pos * digit;
            pos *= 5;
        }

        //System.out.printf("%7s -> %d%n", snafu, val);

        return val;
    }
}
