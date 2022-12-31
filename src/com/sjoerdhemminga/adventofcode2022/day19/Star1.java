package com.sjoerdhemminga.adventofcode2022.day19;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.function.Predicate.not;

public final class Star1 {
    private final String filename;

    private final Map<State, Integer> stateCache = new HashMap<>();

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
            final List<Blueprint> blueprints = lines.filter(not(String::isBlank))
                    .map(Blueprint::parse)
                    .toList();

            final int sum = blueprints.stream()
                    .mapToInt(bp -> {
                        stateCache.clear();
                        final State startState = new State(bp, 24, 0, 0, 0, 0, 1, 0, 0, 0);

                        final int maxGeodeCount = findMaxGeodes(startState);

                        final int qualityLevel = bp.blueprintNumber() * maxGeodeCount;
                        System.out.printf("-> %2d * %4d = %5d%n", bp.blueprintNumber(), maxGeodeCount, qualityLevel);
                        return qualityLevel;
                    })
                    .sum();

            System.out.println("sum = " + sum);
        }

        System.out.println();
    }

    private int findMaxGeodes(final State state) {
        if (stateCache.containsKey(state)) return stateCache.get(state);

        final int count = findMaxGeodesWithoutCache(state);
        stateCache.put(state, count);
        return count;
    }

    private int findMaxGeodesWithoutCache(final State state) {
        //if (state.timeLeft() > 20)
        //    System.out.println("-".repeat(25 - state.timeLeft()) + " time left: " + state.timeLeft());

        if (state.timeLeft() == 0)
            return state.geodeCount();

        int maxGeodes = 0;

        if (state.canBuyExtraGeodeProd() && state.extraGeodeProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraGeodeProd()));

        if (state.canBuyExtraObsidianProd())// && state.extraObsidianProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraObsidianProd()));

        if (state.canBuyExtraClayProd())// && state.extraClayProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraClayProd()));

        if (state.canBuyExtraOreProd())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraOreProd()));

        maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithoutAction()));

        return maxGeodes;
    }
}
