package com.sjoerdhemminga.adventofcode2022.day24;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

public final class Star2 {
    private final String filename;
    private final Set<String> seenHashes = new HashSet<>();

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
            final char[][] gridArr = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            final int entryJ = findOpening(gridArr[0]);
            final int exitJ = findOpening(gridArr[gridArr.length - 1]);

            final Grid grid = new Grid(gridArr.length - 2, gridArr[0].length - 2, entryJ, exitJ);
            final List<Blizzard> blizzards = scanBlizzards(gridArr, grid);

            final State startState = new State(1, entryJ, 0, blizzards, grid);

            final PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::steps));
            queue.add(startState);

            final State stateStage1 = findMinimumSteps(queue, grid, grid.maxI(), grid.exitJ())
                    .next(grid.maxI() + 1, grid.exitJ());
            System.out.println("stateStage1.steps() = " + stateStage1.steps());

            queue.clear();
            queue.add(stateStage1.withSteps(0));

            final State stateStage2 = findMinimumSteps(queue, grid, 1, grid.entryJ())
                    .next(0, grid.entryJ());
            System.out.println("stateStage2.steps() = " + stateStage2.steps());

            queue.clear();
            queue.add(stateStage2.withSteps(0));

            final State stateStage3 = findMinimumSteps(queue, grid, grid.maxI(), grid.exitJ())
                    .next(grid.maxI() + 1, grid.exitJ());
            System.out.println("stateStage3.steps() = " + stateStage3.steps());

            final int totalSteps = stateStage1.steps() + stateStage2.steps() + stateStage3.steps();
            System.out.println("totalSteps = " + totalSteps);
        }

        System.out.println();
    }

    private State findMinimumSteps(final PriorityQueue<State> queue, final Grid grid, final int targetI,
            final int targetJ) {
        seenHashes.clear();
        int minMan = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            final State state = queue.remove();

            final int newMinMan = abs(targetI - state.i()) + abs(targetJ - state.j());

            if (newMinMan < minMan) {
                minMan = newMinMan;
                System.out.printf("Considering state with %3d steps, min man %2d, seenHashes size: %4d, queue size: %d%n",
                        state.steps(), minMan, seenHashes.size(), queue.size());
            }

            if (state.i() == targetI && state.j() == targetJ) {
                //System.out.println(state.dump());

                // Directly above exit
                return state;
            }

            // Skip searching states that have been searched before (with fewer steps too)
            final String hash = state.steplessCode();
            if (seenHashes.contains(hash)) continue;
            seenHashes.add(hash);

            // Wait is always an option
            addState(queue, state.next(state.i(), state.j()));

            if (state.i() == 0 && state.j() == grid.entryJ()) {
                // Start state
                addState(queue, state.next(state.i() + 1, state.j()));
                continue;
            }

            if (state.i() == grid.maxI() + 1 && state.j() == grid.exitJ()) {
                // Start state
                addState(queue, state.next(state.i() - 1, state.j()));
                continue;
            }

            if (state.i() > 1)
                addState(queue, state.next(state.i() - 1, state.j()));
            if (state.i() < grid.maxI())
                addState(queue, state.next(state.i() + 1, state.j()));
            if (state.j() > 1)
                addState(queue, state.next(state.i(), state.j() - 1));
            if (state.j() < grid.maxJ())
                addState(queue, state.next(state.i(), state.j() + 1));
        }

        throw new AssertionError("Not found!");
    }

    private void addState(final PriorityQueue<State> queue, final State state) {
        if (!seenHashes.contains(state.steplessCode()) && state.isValid())
            queue.add(state);
    }

    private List<Blizzard> scanBlizzards(final char[][] gridArr, final Grid grid) {
        final List<Blizzard> blizzards = new ArrayList<>();

        for (int i = 1; i < gridArr.length - 1; i++)
            for (int j = 1; j < gridArr[i].length - 1; j++)
                if (gridArr[i][j] != '.')
                    blizzards.add(new Blizzard(i, j, Direction.fromChar(gridArr[i][j]), grid));

        return blizzards;
    }

    private int findOpening(final char[] row) {
        for (int i = 0; i < row.length; i++)
            if (row[i] == '.')
                return i;
        throw new AssertionError(Arrays.toString(row));
    }
}
