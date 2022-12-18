package com.sjoerdhemminga.adventofcode2022.day17;

import java.util.function.Consumer;

enum BlockInitializer {
    MINUS(block -> { block[3][0] = '@'; block[3][1] = '@'; block[3][2] = '@'; block[3][3] = '@'; }) ,
    PLUS(block -> { block[3][1] = '@'; block[2][0] = '@'; block[2][1] = '@'; block[2][2] = '@'; block[1][1] = '@'; }),
    CORNER(block -> { block[3][0] = '@'; block[3][1] = '@'; block[3][2] = '@'; block[2][2] = '@'; block[1][2] = '@'; }),
    POLE(block -> { block[3][0] = '@'; block[2][0] = '@'; block[1][0] = '@'; block[0][0] = '@'; }),
    SQUARE(block -> { block[3][0] = '@'; block[3][1] = '@'; block[2][0] = '@'; block[2][1] = '@'; });

    private final Consumer<char[][]> blockInit;

    BlockInitializer(final Consumer<char[][]> blockInit) {
        this.blockInit = blockInit;
    }

    void apply(final char[][] block) {
        blockInit.accept(block);
    }

    BlockInitializer next() {
        final int nextOrdinal = (ordinal() + 1) % values().length;
        return values()[nextOrdinal];
    }
}
