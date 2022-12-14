package com.sjoerdhemminga.adventofcode2022.day14;

import java.util.stream.Stream;

record Segment(Ij start, Ij end) {
    static Segment parse(final String[] points) {
        return new Segment(Ij.parse(points[0]), Ij.parse(points[1]));
    }

    Stream<Ij> coords() {
        return Stream.of(start, end);
    }
}
