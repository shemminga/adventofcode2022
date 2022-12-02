package com.sjoerdhemminga.adventofcode2022.day02;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final int ROCK = 0;
    private static final int PAPER = 1;
    private static final int SCISSORS = 2;

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star1.class.getResource("input.txt");

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final int[][] ints = lines.filter(not(String::isBlank))
                    .map(line -> new int[]{line.charAt(0), line.charAt(2)})
                    .toList()
                    .toArray(int[][]::new);

            int score = 0;
            for (final int[] round : ints)
                score += outcomeScore(round[0], round[1]) + moveScore(round[1]);

            System.out.println(score);
        }
    }

    private static int moveScore(final int me) {
        return switch (me - 'X') {
            case ROCK -> 1;
            case PAPER -> 2;
            case SCISSORS -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + (me - 'X'));
        };
    }

    private static int outcomeScore(final int other, final int me) {
        final int rpsOth = other - 'A';
        final int rpsMe = me - 'X';

        return switch (rpsMe) {
            case ROCK -> switch (rpsOth) {
                case ROCK -> 3;
                case PAPER -> 0;
                case SCISSORS -> 6;
                default -> throw new IllegalStateException("Unexpected value: " + rpsOth);
            };
            case PAPER -> switch (rpsOth) {
                case ROCK -> 6;
                case PAPER -> 3;
                case SCISSORS -> 0;
                default -> throw new IllegalStateException("Unexpected value: " + rpsOth);
            };
            case SCISSORS -> switch (rpsOth) {
                case ROCK -> 0;
                case PAPER -> 6;
                case SCISSORS -> 3;
                default -> throw new IllegalStateException("Unexpected value: " + rpsOth);
            };
            default -> throw new IllegalStateException("Unexpected value: " + rpsMe);
        };
    }
}
