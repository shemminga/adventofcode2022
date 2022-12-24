package com.sjoerdhemminga.adventofcode2022.day22;

record IjFacing(int i, int j, int facing) {
    private IjFacing withI(final int newI) {
        return new IjFacing(newI, j(), facing());
    }

    private IjFacing withJ(final int newJ) {
        return new IjFacing(i(), newJ, facing());
    }

    static IjFacing fromCoordAndFacing(final int[] coord, final int facing) {
        return new IjFacing(coord[0], coord[1], facing);
    }
    int[] coord() {
        return new int[]{i, j};
    }
}
