package com.sjoerdhemminga.adventofcode2022.meilisearch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Part2 {
    private static final Pattern SEPARATOR = Pattern.compile(" - ");

    private final String filename;
    private final Node2 root = new Node2(null, "", "o");
    private int totalSteps;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Part2("input-test.txt").doFile();
        new Part2("input.txt").doFile();
    }

    private Part2(final String filename) {
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

            mergeNodes(root);
            //dumpNodes(root, "");
            calcTotalSteps();

            System.out.println("totalSteps = " + totalSteps);
        }

        System.out.println();
    }

    private void calcTotalSteps() {
        Node2 cur = root;
        while (cur != null) {
            //System.out.printf("`%5d %s%n", totalSteps, cur.getNames());
            cur = findNearest(cur);
        }
    }

    private Node2 findNearest(final Node2 from) {
        final Map<Node2, Integer> dists = new HashMap<>();
        dists.put(from, 0);

        final Deque<Node2> q = new ArrayDeque<>();
        q.add(from);

        final Set<Node2> found = new HashSet<>();
        int closestDist = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            final Node2 cur = q.removeFirst();
            final int curCnt = dists.get(cur);

            if (curCnt <= closestDist && cur.hasNames() && !cur.isVisited()) {
                closestDist = curCnt;
                found.add(cur);
            }

            addIfNotHandled(cur.getL(), q, dists, curCnt);
            addIfNotHandled(cur.getR(), q, dists, curCnt);
            addIfNotHandled(cur.getParent(), q, dists, curCnt);
        }

        final Optional<Node2> min = found.stream()
                .min(Comparator.comparing(Node2::getFullPath));

        min.ifPresent(node -> {
            totalSteps += dists.get(node);
            node.setVisited(true);
        });

        return min.orElse(null);
    }

    private void addIfNotHandled(final Node2 node, final Deque<Node2> q, final Map<Node2, Integer> dists,
            final int curCnt) {
        if (node == null) return;
        if (dists.containsKey(node)) return;

        dists.put(node, curCnt + 1);
        q.addLast(node);
    }

    private void dumpNodes(final Node2 cur, final String prefix) {
        if (cur == null) return;

        System.out.printf("%s- %s ----- %s%n", prefix, cur.getPathPart(), cur.getNames());
        dumpNodes(cur.getL(), prefix + "``|");
        dumpNodes(cur.getR(), prefix + "``|");
    }

    private void mergeNodes(final Node2 cur) {
        if (cur == null) return;

        mergeNodes(cur.getL());
        mergeNodes(cur.getR());
        cur.mergeIfNeeded();
    }

    private void add(final String name, final String route) {
        Node2 cur = root;
        for (final char c : route.toCharArray()) {
            cur = switch (c) {
                case 'L' -> cur.ensureL();
                case 'R' -> cur.ensureR();
                default -> throw new IllegalStateException("Unexpected value: " + c);
            };
        }

        cur.addName(name);
    }
}
