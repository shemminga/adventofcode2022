package com.sjoerdhemminga.adventofcode2022.day24;

enum Direction {
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

    private final char ch;

    Direction(final char ch) {
        this.ch = ch;
    }

    char getCh() {
        return ch;
    }

    static Direction fromChar(final char c) {
        for (final Direction d : values()) if (d.ch == c) return d;
        throw new AssertionError(c);
    }
}
