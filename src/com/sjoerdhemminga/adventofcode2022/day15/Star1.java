package com.sjoerdhemminga.adventofcode2022.day15;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern PATTERN =
            Pattern.compile("^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$");

    private final String filename;
    private final int targetJ;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-test.txt", 10).doFile();
        new Star1("input.txt", 2_000_000).doFile();
    }

    private Star1(final String filename, final int targetJ) {
        this.filename = filename;
        this.targetJ = targetJ;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final List<int[]> meas = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(m -> new String[]{m.group(1), m.group(2), m.group(3), m.group(4)})
                    .map(s -> new int[]{parseInt(s[0]), parseInt(s[1]), parseInt(s[2]), parseInt(s[3])})
                    .toList();

            final Comparator<Object> comparator = Comparator
                    .comparingInt(r -> ((int[]) r)[0])
                    .thenComparingInt(r -> ((int[]) r)[1]);
            final Deque<int[]> isects = meas.stream()
                    .map(this::intersection)
                    .filter(Objects::nonNull)
                    .sorted(comparator)
                    .collect(Collectors.toCollection(ArrayDeque::new));

            final Deque<int[]> combined = combine(isects);

            final Deque<Integer> beacons = meas.stream()
                    .filter(m -> m[3] == targetJ)
                    .mapToInt(m -> m[2])
                    .sorted()
                    .distinct()
                    .boxed()
                    .collect(Collectors.toCollection(ArrayDeque::new));

            final int count = count(combined, beacons);
            System.out.println("count = " + count);
        }

        System.out.println();
    }

    private int count(final Deque<int[]> combined, final Deque<Integer> beacons) {
        int count = 0;
        while (!combined.isEmpty()) {
            final int[] range = combined.removeFirst();
            System.out.printf("%2d - %2d ", range[0], range[1]);
            count += range[1] - range[0] + 1;
            if (!beacons.isEmpty() && beacons.peekFirst() <= range[1]) {
                final int beacon = beacons.removeFirst();
                if (beacon >= range[0]) {
                    System.out.print('R');
                    count--;
                }
                System.out.printf("%2d ", beacon);
            }
        }
        System.out.println();
        return count;
    }

    private Deque<int[]> combine(final Deque<int[]> isects) {
        final Deque<int[]> combined = new ArrayDeque<>();

        while (!isects.isEmpty()) {
            final int[] first = isects.removeFirst();

            if (isects.isEmpty()) {
                combined.addLast(first);
            } else {
                final int[] second = isects.removeFirst();

                if (second[0] <= first[1] + 1) {
                    if (second[1] > first[1] + 1) isects.addFirst(new int[]{first[0], second[1]});
                    else isects.addFirst(new int[]{first[0], first[1]});
                } else {
                    combined.addLast(first);
                    isects.addFirst(second);
                }
            }
        }

        return combined;
    }

    private int[] intersection(final int[] meas) {
        final int manhattan = manhattan(meas[0], meas[1], meas[2], meas[3]);
        final int dist = abs(targetJ - meas[1]);

        if (dist > manhattan) return null;
        final int dev = manhattan - dist;
        return new int[]{meas[0] - dev, meas[0] + dev};
    }

    private int manhattan(final int i1, final int j1, final int i2, final int j2) {
        return abs(i1 - i2) + abs(j1 - j2);
    }
}
