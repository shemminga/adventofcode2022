package com.sjoerdhemminga.adventofcode2022.day23;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

record Ij(int i, int j) {
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

    private Ij withI(final int newI) {
        return new Ij(newI, j());
    }

    private Ij withJ(final int newJ) {
        return new Ij(i(), newJ);
    }

    Set<Ij> getAllNeighbours() {
        final Set<Ij> all = new HashSet<>();
        for (final Direction dir : Direction.values()) all.addAll(getDirection(dir));
        return all;
    }

    Set<Ij> getDirection(final Direction direction) {
        return switch (direction) {
            case NORTH -> getNorth();
            case SOUTH -> getSouth();
            case WEST -> getWest();
            case EAST -> getEast();
        };
    }

    private Set<Ij> getNorth() {
        final Ij center = withI(i - 1);
        return Set.of(center.withJ(j - 1), center, center.withJ(j + 1));
    }

    private Set<Ij> getSouth() {
        final Ij center = withI(i + 1);
        return Set.of(center.withJ(j - 1), center, center.withJ(j + 1));
    }

    private Set<Ij> getWest() {
        final Ij center = withJ(j - 1);
        return Set.of(center.withI(i - 1), center, center.withI(i + 1));
    }

    private Set<Ij> getEast() {
        final Ij center = withJ(j + 1);
        return Set.of(center.withI(i - 1), center, center.withI(i + 1));
    }

    Ij move(final Direction direction) {
        return switch (direction) {
            case NORTH -> withI(i - 1);
            case SOUTH -> withI(i + 1);
            case WEST -> withJ(j - 1);
            case EAST -> withJ(j + 1);
        };
    }

}
