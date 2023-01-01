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

/** Run with -Xms32G -Xmx32G */
public final class Star2 {
    private final String filename;

    private static final int[]
            UPPER_BOUNDS = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105, 120, 136, 153, 171, 190, 210, 231,
            253, 276, 300, 325, 351, 378, 406, 435, 465, 496, 528};

    private final Map<State2, Integer> stateCache = new HashMap<>(100_000_000);

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
            final List<Blueprint> blueprints = lines.filter(not(String::isBlank))
                    .limit(3)
                    .map(Blueprint::parse)
                    .toList();

            final long product = blueprints.stream()
                    .mapToLong(bp -> {
                        stateCache.clear();
                        final State2 startState = new State2(bp, 32, 0, 0, 0, 0, 1, 0, 0, 0);

                        final int maxGeodeCount = findMaxGeodes(startState, 0);
                        System.out.printf("-> %2d -> %4d%n", bp.blueprintNumber(), maxGeodeCount);
                        return maxGeodeCount;
                    })
                    .reduce(1, (x, y) -> x * y);

            System.out.println("product = " + product);
        }

        System.out.println();
    }

    private int findMaxGeodes(final State2 state, final int currentMax) {
        if (stateCache.containsKey(state)) return stateCache.get(state);

        final int count = findMaxGeodesWithoutCache(state, currentMax);
        stateCache.put(state, count);
        return count;
    }

    private int findMaxGeodesWithoutCache(final State2 state, final int currentMax) {
        //if (state.timeLeft() > 20)
        //    System.out.println("-".repeat(25 - state.timeLeft()) + " time left: " + state.timeLeft());

        if (state.timeLeft() >= 2 && UPPER_BOUNDS[state.timeLeft() - 2] + state.geodeCount() + state.timeLeft() *
                state.geodeCount() < currentMax) {
            return -1;
        }

        if (state.timeLeft() == 0)
            return state.geodeCount();

        int maxGeodes = 0;

        if (state.canBuyExtraGeodeProd() && state.extraGeodeProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraGeodeProd(), max(currentMax, maxGeodes)));

        if (state.canBuyExtraObsidianProd() && state.extraObsidianProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraObsidianProd(), max(currentMax, maxGeodes)));

        if (state.canBuyExtraClayProd() && state.extraClayProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraClayProd(), max(currentMax, maxGeodes)));

        if (state.canBuyExtraOreProd() && state.extraOreProdIsUseful())
            maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithExtraOreProd(), max(currentMax, maxGeodes)));

        maxGeodes = max(maxGeodes, findMaxGeodes(state.nextWithoutAction(), max(currentMax, maxGeodes)));

        return maxGeodes;
    }
}
