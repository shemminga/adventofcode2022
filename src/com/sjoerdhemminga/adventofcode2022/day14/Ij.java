package com.sjoerdhemminga.adventofcode2022.day14;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

record Ij(int i, int j) {
    static Ij parse(final String string) {
        final String[] split = string.split(",");
        final int splitI = parseInt(split[0].trim());
        final int splitJ = parseInt(split[1].trim());

        return new Ij(splitI, splitJ);
    }

    static Ij minBound(final Collection<Ij> ijs) {
        return ijs.stream()
                .reduce((ij1, ij2) -> new Ij(min(ij1.i(), ij2.i()), min(ij1.j(), ij2.j())))
                .orElseThrow();
    }

    static Ij maxBound(final Collection<Ij> ijs) {
        return ijs.stream()
                .reduce((ij1, ij2) -> new Ij(max(ij1.i(), ij2.i()), max(ij1.j(), ij2.j())))
                .orElseThrow();
    }

    Ij withI(final int newI) {
        return new Ij(newI, j());
    }

    Ij withJ(final int newJ) {
        return new Ij(i(), newJ);
    }

    Stream<Ij> streamTo(final Ij endIj) {
        return streamToByRow(endIj)
                .flatMap(x -> x);
    }

    Stream<Stream<Ij>> streamToByRow(final Ij endIj) {
        final int minI = min(i(), endIj.i());
        final int maxI = max(i(), endIj.i());
        final int minJ = min(j(), endIj.j());
        final int maxJ = max(j(), endIj.j());

        return IntStream.rangeClosed(minI, maxI)
                .mapToObj(curI -> IntStream.rangeClosed(minJ, maxJ)
                        .mapToObj(curJ -> new Ij(curI, curJ)));
    }
}
