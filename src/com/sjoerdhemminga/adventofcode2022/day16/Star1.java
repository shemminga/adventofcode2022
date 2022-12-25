package com.sjoerdhemminga.adventofcode2022.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.function.Predicate.not;

public final class Star1 {
    private final String filename;
    private final Set<String> openValves = new HashSet<>();
    private Map<String, Valve> valves;
    private List<String> importantValves;
    private Map<String, Map<String, Integer>> importantMatrix;

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
            valves = lines.filter(not(String::isBlank))
                    .map(Valve::parse)
                    .collect(Collectors.toMap(Valve::label, x -> x));

            importantValves = Stream.concat(Stream.of("AA"),
                            valves.values()
                                    .stream()
                                    .filter(v -> v.flowRate() > 0)
                                    .map(Valve::label))
                    .sorted()
                    .toList();

            importantMatrix = new HashMap<>();

            for (final String iValve : importantValves) {
                final Map<String, Integer> distances = dijkstra(iValve);
                importantMatrix.put(iValve, distances);
            }

            System.out.println("importantValves = " + importantValves);
            System.out.println("importantMatrix = " + importantMatrix);

            final int totalReleased = search("AA", importantValves, 0, 30);
            System.out.println("totalReleased = " + totalReleased);
        }

        System.out.println();
    }

    private int search(final String curValve, final List<String> valvesLeft, final int flowRate, final int timeLeft) {
        if (timeLeft <= 0) throw new AssertionError(timeLeft);

        final List<String> newValvesLeft = valvesLeft.stream()
                .filter(not(curValve::equals))
                .toList();

        final int newTimeLeft = "AA".equals(curValve) ? timeLeft : timeLeft - 1;
        final int newFlowRate = flowRate + valves.get(curValve).flowRate();

        int maxReleased = 0;
        maxReleased += flowRate; // Released while opening
        maxReleased += newFlowRate * newTimeLeft; // Released while just walking around

        for (final String next : newValvesLeft) {
            final int travelTime = importantMatrix.get(curValve).get(next);
            if (travelTime + 1 > newTimeLeft) continue; // Can't open it in time

            int released = 0;
            released += flowRate; // Released while opening
            released += newFlowRate * travelTime; // Released while traveling
            released += search(next, newValvesLeft, newFlowRate, newTimeLeft - travelTime);

            maxReleased = max(maxReleased, released);
        }

        return maxReleased;
    }

    private Map<String, Integer> dijkstra(final String start) {
        final Map<String, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        final PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(start);

        while (!queue.isEmpty()) {
            final String cur = queue.remove();
            final int curDist = distances.get(cur);
            final Valve curValve = valves.get(cur);

            curValve.connectedValves().forEach(next -> {
                if (distances.getOrDefault(next, Integer.MAX_VALUE) > curDist + 1) {
                    distances.put(next, curDist + 1);
                    queue.remove(next);
                    queue.add(next);
                }
            });
        }

        return importantValves.stream()
                .filter(not(start::equals))
                .collect(Collectors.toMap(l -> l, distances::get));
    }
}
