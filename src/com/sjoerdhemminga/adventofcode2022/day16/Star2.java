package com.sjoerdhemminga.adventofcode2022.day16;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2022.day16.Mover.START_MOVER;
import static java.lang.Math.max;
import static java.util.function.Predicate.not;

public final class Star2 {
    private final String filename;
    private Map<String, Valve> valves;
    private List<String> importantValves;
    private Map<String, Map<String, Integer>> importantMatrix;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-simple1.txt").doFile();
        new Star2("input-simple2.txt").doFile();
        new Star2("input-simple3.txt").doFile();
        new Star2("input-simple4.txt").doFile();
        new Star2("input-simple5.txt").doFile();
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

            final List<String> valvesLeft = importantValves.stream()
                    .filter(not("AA"::equals))
                    .toList();

            final State startState = new State(START_MOVER, START_MOVER, valvesLeft, 0, 26);
            final int totalReleased = search(startState);

            System.out.println("totalReleased = " + totalReleased);
        }

        System.out.println();
    }

    private int search(final State state) {
        state.assertState();

        if (state.allStopped()) {
            final int released = state.timeLeft() * state.flowRate();
            //System.out.printf("L %4d %s%n", released, state);
            return released;
        }

        final Mover arriver = state.arriver();

        final int newFlowRate;
        {
            final int extraFlowRate = valves.get(arriver.pos()).flowRate();
            newFlowRate = state.flowRate() + extraFlowRate;
        }

        int maxReleased = 0;
        { // Result when arriver stops moving
            final State nextState = state.stopMoving(newFlowRate);
            maxReleased = search(state, nextState, newFlowRate);
        }

        for (final String nextValve : state.valvesLeft()) {
            final int timeToOpenNextValve = importantMatrix.get(arriver.pos()).get(nextValve) + 1;
            if (timeToOpenNextValve >= state.timeLeft()) continue; // Pointless to move if you don't reach it in time

            final State nextState = state.travelTo(nextValve, timeToOpenNextValve, newFlowRate);
            final int released = search(state, nextState, newFlowRate);
            maxReleased = max(maxReleased, released);
        }

        //System.out.printf("- %4d %s%n", maxReleased, state);

        return maxReleased;
    }

    private int search(final State oldState, final State nextState, final int newFlowRate) {
        final int timeToNextState = oldState.timeLeft() - nextState.timeLeft();
        final int releasedUntilNextState = timeToNextState * newFlowRate;
        return releasedUntilNextState + search(nextState);
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
