package com.sjoerdhemminga.adventofcode2022.meilisearch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Part1 {
    private static final Pattern SEPARATOR = Pattern.compile(" - ");

    private final String filename;
    private final Node1 root = new Node1();

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Part1("input-test.txt").doFile();
        new Part1("input.txt").doFile();
    }

    private Part1(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Part1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(SEPARATOR::split)
                    .forEachOrdered(s -> add(s[0], s[1]));

            final Node1 nearestNode = findNearestNode(root, 0);

            //dumpNodes(root, "");

            final int dist = nearestNode.getDist();
            final List<String> names = nearestNode.getNames();
            System.out.println("dist = " + dist);
            System.out.println("names = " + names);
        }

        System.out.println();
    }

    private void dumpNodes(final Node1 cur, final String prefix) {
        if (cur == null) {
            System.out.printf("%snull%n", prefix);
            return;
        }

        System.out.printf("%s[dist %2d; names %s]%n", prefix, cur.getDist(), cur.getNames());
        dumpNodes(cur.getL(), prefix + "| L: ");
        dumpNodes(cur.getR(), prefix + "| R: ");
    }

    private Node1 findNearestNode(final Node1 cur, final int dist) {
        cur.setDist(dist);

        int setCnt = 0;
        setCnt += cur.getL() == null ? 0 : 1;
        setCnt += cur.getR() == null ? 0 : 1;
        setCnt += cur.getNames().isEmpty() ? 0 : 1;

        final int newDist = (setCnt > 1) ? dist + 1 : dist;

        final Node1 lNearest = cur.getL() == null ? null : findNearestNode(cur.getL(), newDist);
        final Node1 rNearest = cur.getR() == null ? null : findNearestNode(cur.getR(), newDist);

        if (!cur.getNames().isEmpty()) return cur;
        if (lNearest == null) return rNearest == null ? cur : rNearest;
        if (rNearest == null) return lNearest;
        if (rNearest.getDist() < lNearest.getDist()) return rNearest;
        return lNearest;
    }

    private void add(final String name, final String route) {
        Node1 cur = root;
        for (int i = 0; i < route.length(); i++)
            cur = switch (route.charAt(i)) {
                case 'L' -> cur.ensureL();
                case 'R' -> cur.ensureR();
                default -> throw new IllegalStateException("Unexpected value: " + route.charAt(i));
            };

        cur.addName(name);
    }
}
