package com.sjoerdhemminga.adventofcode2022.day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;

public final class Star2 {
    private static final String DIVIDER2 = "[[2]]";
    private static final String DIVIDER6 = "[[6]]";
    private final String filename;
    //private int indentLevel = 0;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-test.txt").doFile();
        new Star2("input.txt").doFile();
    }

    private Star2(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final Deque<String> inputDeque = lines
                    .filter(not(String::isBlank))
                    .collect(Collectors.toCollection(ArrayDeque::new));

            inputDeque.addFirst(DIVIDER2);
            inputDeque.addLast(DIVIDER6);

            final List<String> sortedPackets = inputDeque.stream()
                    .sorted(this::comparePacket)
                    .toList();
            //System.out.println("sortedPackets = " + sortedPackets);

            final int decoderKey = (sortedPackets.indexOf(DIVIDER2) + 1) * (sortedPackets.indexOf(DIVIDER6) + 1);
            System.out.println("decoderKey = " + decoderKey);
        }

        System.out.println();
    }

    //private int comparePacket(final String first, final String second) {
    //    System.out.printf("%s- Compare %s vs %s%n", " ".repeat(indentLevel), first, second);
    //    indentLevel++;
    //    final int result = comparePacket1(first, second);
    //    System.out.printf("%s -> %2d%n", " ".repeat(indentLevel), result);
    //    indentLevel--;
    //    return result;
    //}

    private int comparePacket(final String first, final String second) {
        if (first.isEmpty() || second.isEmpty())
            return Integer.compare(first.length(), second.length());

        if (first.charAt(0) != '[' && second.charAt(0) != '[')
            return Integer.compare(parseInt(first), parseInt(second));

        if (first.charAt(0) != '[')
            return comparePacket('[' + first + ']', second);

        if (second.charAt(0) != '[')
            return comparePacket(first, '[' + second + ']');

        final String[] firstParts = breakList(first);
        final String[] secondParts = breakList(second);

        for (int i = 0; i < min(firstParts.length, secondParts.length); i++) {
            final int compRes = comparePacket(firstParts[i], secondParts[i]);
            if (compRes != 0) return compRes;
        }

        return Integer.compare(firstParts.length, secondParts.length);
    }

    private static String[] breakList(final String list) {
        final List<String> parts = new ArrayList<>();

        int brace = 0;
        int lastComma = 0;
        for (int i = 1; i < list.length() - 1; i++) {
            final char c = list.charAt(i);
            if (c == '[') brace++;
            if (c == ']') brace--;
            if (brace > 0) continue;
            if (c == ',') {
                final String part = list.substring(lastComma + 1, i);
                parts.add(part);
                lastComma = i;
            }
        }

        final String part = list.substring(lastComma + 1, list.length() - 1);
        parts.add(part);

        return parts.toArray(String[]::new);
    }
}
