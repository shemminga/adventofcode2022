package com.sjoerdhemminga.adventofcode2022.day17;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public final class Star1 {
    private static final char[] EMPTY_ROW = new char[7];
    private final String filename;
    private final List<char[]> chamber = new ArrayList<>();
    private int highestLevel = 0;

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
            final char[] jets = lines.findFirst()
                    .orElseThrow()
                    .toCharArray();

            int currentJet = 0;
            BlockInitializer initializer = BlockInitializer.MINUS;

            for (int blockCount = 0; blockCount < 2022; blockCount++) {
                ensureHeight(highestLevel + 3 + 4);
                final Block block = new Block(initializer, highestLevel + 3);

                //boolean firstIteration = true;

                do {
                    //if ((firstIteration && blockCount <= 10) || blockCount <= 1) {
                    //    dump(block);
                    //    firstIteration = true;
                    //}

                    block.moveHorizontal(chamber, jets[currentJet]);
                    currentJet = (currentJet + 1) % jets.length;

                    //if (blockCount <= 1) {
                    //    dump(block);
                    //}
                } while (block.moveDown(chamber));

                block.rest(chamber);
                highestLevel = highestLevel();
                initializer = initializer.next();
            }

            System.out.println("highestLevel = " + highestLevel);
        }

        System.out.println();
    }

    private void dump(final Block block) {
        final StringBuilder sb = new StringBuilder();

        for (int i = chamber.size() - 1; i >= 0; i--) {
            sb.append('|');

            final char[] chamberRow = chamber.get(i);
            final char[] blockRow = getBlockRow(block, i);
            final int leftCol = block.getLeftCol();

            for (int j = 0; j < chamberRow.length; j++) {
                final char chamberCell = chamberRow[j];
                final char blockCell = j >= leftCol && j < leftCol + 4 ? blockRow[j - leftCol] : 0;

                sb.append((char) (chamberCell + blockCell));
            }

            sb.append("|\n");
        }

        sb.append("+-------+\n");
        System.out.println(sb);
    }

    private static char[] getBlockRow(final Block block, final int i) {
        if (i == block.getBottomRow()) return block.getBlock()[3];
        if (i == block.getBottomRow() + 1) return block.getBlock()[2];
        if (i == block.getBottomRow() + 2) return block.getBlock()[1];
        if (i == block.getBottomRow() + 3) return block.getBlock()[0];
        return EMPTY_ROW;
    }

    private void ensureHeight(final int height) {
        for (int i = chamber.size(); chamber.size() < height; i++) {
            chamber.add(new char[7]);
        }
    }

    private int highestLevel() {
        for (int i = chamber.size() - 1; i >= 0; i--) {
            if (!Arrays.equals(chamber.get(i), EMPTY_ROW)) return i + 1;
        }
        return 0;
    }
}
