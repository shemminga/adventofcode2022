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

public final class Star1 {
    private final String filename;
    private int indentLevel = 0;

    public static void main(final String... args) throws IOException, URISyntaxException {
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
            final Deque<String> inputDeque = lines
                    .collect(Collectors.toCollection(ArrayDeque::new));

            int idx = 1;
            int sortedIdxSum = 0;
            while (!inputDeque.isEmpty()) {
                final String first = inputDeque.removeFirst();
                final String second = inputDeque.removeFirst();
                if (!inputDeque.isEmpty()) inputDeque.removeFirst(); // Separator line

                if (comparePacket(first, second) == -1)
                    sortedIdxSum += idx;

                idx++;
            }

            System.out.println("sortedIdxSum = " + sortedIdxSum);
        }

        System.out.println();
    }

    private int comparePacket(final String first, final String second) {
        System.out.printf("%s- Compare %s vs %s%n", " ".repeat(indentLevel), first, second);
        indentLevel++;
        final int result = comparePacket1(first, second);
        System.out.printf("%s -> %2d%n", " ".repeat(indentLevel), result);
        indentLevel--;
        return result;
    }

    private int comparePacket1(final String first, final String second) {
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
