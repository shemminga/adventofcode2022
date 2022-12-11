package com.sjoerdhemminga.adventofcode2022.day11;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public final class Star1 {
    private static final Pattern PATTERN = Pattern.compile(", ");

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1().doFile("input-test.txt");
        new Star1().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Monkey[] monkeys = parse(lines.collect(Collectors.toCollection(ArrayDeque::new)));
            final int[] activityCounters = new int[monkeys.length];

            dumpItemsAndActivityCounters("At start", monkeys, activityCounters);

            for (int round = 1; round <= 20; round++) {
                playRound(monkeys, activityCounters);
                dumpItemsAndActivityCounters("After round " + round, monkeys, activityCounters);
            }

            final int[] sortedActivityCounters = Arrays.stream(activityCounters)
                    .sorted()
                    .toArray();

            final int monkeyBusinessLevel = sortedActivityCounters[sortedActivityCounters.length - 1] *
                    sortedActivityCounters[sortedActivityCounters.length - 2];

            System.out.println();
            System.out.println("+++ monkeyBusinessLevel = " + monkeyBusinessLevel);
        }

        System.out.println();
    }

    static void dumpItemsAndActivityCounters(final String header, final Monkey[] monkeys,
            final int[] activityCounters) {
        System.out.println();
        System.out.println(header);
        System.out.println("-".repeat(header.length()));

        for (int i = 0; i < monkeys.length; i++) {
            System.out.printf("Monkey %d (%3d) (%% %2d): %s%n",
                    i,
                    activityCounters[i],
                    monkeys[i].divisableByTest(),
                    monkeys[i].items());
        }
    }

    static void playRound(final Monkey[] monkeys, final int[] activityCounters) {
        for (int i = 0; i < monkeys.length; i++) {
            while (monkeys[i].hasItemToThrow()) {
                activityCounters[i]++;
                final long itemReadyToThrow = monkeys[i].getItemReadyToThrowStar1();
                final int target = monkeys[i].getTarget(itemReadyToThrow);
                monkeys[target].items().addLast(itemReadyToThrow);
                //System.out.printf("S1 %2d -> %2d %5d (%5d)%n",
                //        i,
                //        target,
                //        itemReadyToThrow % monkeys[i].divisableByTest(),
                //        itemReadyToThrow);
            }
        }
    }

    static Monkey[] parse(final Deque<String> lines) {
        final List<Monkey> monkeys = new ArrayList<>();
        while (!lines.isEmpty())
            monkeys.add(parseOneMonkey(lines));
        return monkeys.toArray(Monkey[]::new);
    }

    private static Monkey parseOneMonkey(final Deque<String> lines) {
        lines.removeFirst(); // Unneeded start line

        final Deque<Long> items = parseItems(lines);
        final Function<Long, Long> operation = parseOperation(lines);
        final long divisableByTest = parseLong(lines.removeFirst().substring(21));
        final int trueTarget = parseInt(lines.removeFirst().substring(29));
        final int falseTarget = parseInt(lines.removeFirst().substring(30));

        if (!lines.isEmpty()) lines.removeFirst(); // Separator line

        return new Monkey(items, operation, divisableByTest, trueTarget, falseTarget);
    }

    private static Function<Long, Long> parseOperation(final Deque<String> lines) {
        final String opLine = lines.removeFirst();
        final char operation = opLine.charAt(23);
        final String operand2 = opLine.substring(25);

        if ("old".equals(operand2)) {
            return switch (operation) {
                case '+' -> x -> x + x;
                case '*' -> x -> x * x;
                default -> throw new AssertionError(operation);
            };
        }

        final long parsedOperand2 = parseLong(operand2);

        return switch (operation) {
            case '+' -> x -> x + parsedOperand2;
            case '*' -> x -> x * parsedOperand2;
            default -> throw new AssertionError(operation);
        };
    }

    private static Deque<Long> parseItems(final Deque<String> lines) {
        final String itemsLine = lines.removeFirst();
        final String[] itemsStr = PATTERN.split(itemsLine.substring(18));
        return Arrays.stream(itemsStr)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toCollection(ArrayDeque::new));
    }
}
