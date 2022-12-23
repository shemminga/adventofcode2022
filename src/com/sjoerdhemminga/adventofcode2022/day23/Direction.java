package com.sjoerdhemminga.adventofcode2022.day23;

enum Direction {
    NORTH, SOUTH, WEST, EAST;

    Direction next() {
        final int nextOrdinal = (ordinal() + 1) % values().length;
        return values()[nextOrdinal];
    }
}
