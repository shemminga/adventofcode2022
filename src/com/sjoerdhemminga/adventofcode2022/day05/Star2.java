package com.sjoerdhemminga.adventofcode2022.day05;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.function.Predicate.not;

public final class Star2 {
    private static final Deque<int[]> MOVES = new ArrayDeque<>();
    private static final Deque<Character>[] STACKS = new Deque[10];

    public static void main(final String... args) throws IOException, URISyntaxException {
        final URL input = Star2.class.getResource("input.txt");

        for (int i = 0; i < STACKS.length; i++) STACKS[i] = new ArrayDeque<>();

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .forEachOrdered(Star2::parse);

            dumpState("Before");

            while (!MOVES.isEmpty()) {
                final int[] move = MOVES.removeFirst();

                final Deque<Character> tmpStack = new ArrayDeque<>();

                for (int i = 0; i < move[0]; i++)
                    tmpStack.addFirst(STACKS[move[1]].removeFirst());

                while (!tmpStack.isEmpty())
                    STACKS[move[2]].addFirst(tmpStack.removeFirst());
            }

            dumpState("After");

            System.out.print("Solution = ");
            for (final Deque<Character> stack : STACKS) if (!stack.isEmpty()) System.out.print(stack.peekFirst());
            System.out.println();
        }
    }

    private static void dumpState(final String header) {
        System.out.println(header);
        System.out.println("-".repeat(header.length()));

        for (int i = 0; i < STACKS.length; i++) {
            System.out.println("i = " + i + " STACKS[i] = " + STACKS[i]);
        }

        for (final int[] move : MOVES) {
            System.out.println("Arrays.toString(move) = " + Arrays.toString(move));
        }

        System.out.println();
    }

    private static void parse(final String line) {
        if (line.charAt(0) == 'm') {
            final String[] split = line.split(" ");
            MOVES.addLast(new int[] {
                    parseInt(split[1]),
                    parseInt(split[3]),
                    parseInt(split[5])
            });
            return;
        }

        if (line.charAt(1) == '1') return;

        for (int i = 1, stack = 1; i < line.length(); i += 4, stack++)
            if (line.charAt(i) >= 'A' && line.charAt(i) <= 'Z')
                STACKS[stack].addLast(line.charAt(i));
    }
}
