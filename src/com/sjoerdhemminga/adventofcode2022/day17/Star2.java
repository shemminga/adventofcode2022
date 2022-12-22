package com.sjoerdhemminga.adventofcode2022.day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Map.entry;

public final class Star2 {
    private static final long MAX_BLOCK_COUNT = 1_000_000_000_000L;
    private final String filename;
    private final Chamber chamber = new Chamber();
    private long highestLevel = 0;

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
            final char[] jets = lines.findFirst()
                    .orElseThrow()
                    .toCharArray();

            int currentJet = 0;
            BlockInitializer initializer = BlockInitializer.MINUS;
            final Map<String, Entry<Long, Long>> states = new HashMap<>();

            for (long blockCount = 0; blockCount < MAX_BLOCK_COUNT; blockCount++) {
                chamber.ensureHeight(highestLevel + 3 + 4);
                final BlockStar2 block = new BlockStar2(initializer, highestLevel + 3);

                do {
                    block.moveHorizontal(chamber, jets[currentJet]);
                    currentJet = (currentJet + 1) % jets.length;
                } while (block.moveDown(chamber));

                block.rest(chamber);

                final String stateCode = currentJet + initializer.name() + chamber.stateCode();
                final Entry<Long, Long> state = entry(blockCount, chamber.bottom());

                if (states.containsKey(stateCode)) {
                    final Entry<Long, Long> prevState = states.get(stateCode);

                    System.out.println("stateCode = " + stateCode);
                    System.out.printf("state = %s, prevState = %s%n", state, prevState);

                    final long blocksInRound = blockCount - prevState.getKey();
                    final long rowsInRound = chamber.bottom() - prevState.getValue();
                    final long remainingBlocks = MAX_BLOCK_COUNT - blockCount - 1;

                    System.out.printf("blocksInRound = %d, rowsInRound = %d, remainingBlocks = %d%n",
                            blocksInRound, rowsInRound, remainingBlocks);

                    final long skipRounds = remainingBlocks / blocksInRound;
                    final long addBlocks = skipRounds * blocksInRound;
                    blockCount += addBlocks;
                    final long raiseRows = skipRounds * rowsInRound;
                    chamber.raise(raiseRows);

                    states.clear();

                    System.out.printf("Found state, skipping %d rounds, adding %d blocks, raising %d rows%n",
                            skipRounds, addBlocks, raiseRows);
                } else {
                    states.put(stateCode, state);
                }

                highestLevel = chamber.highestLevel();
                initializer = initializer.next();
            }

            System.out.println("highestLevel = " + highestLevel);
        }

        System.out.println();
    }
}
