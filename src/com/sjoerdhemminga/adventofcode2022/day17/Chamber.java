package com.sjoerdhemminga.adventofcode2022.day17;

import java.util.Arrays;
import java.util.stream.Collectors;

final class Chamber {
    private static final char[] EMPTY_ROW = new char[7];

    private final CoolArrayList<char[]> chamber = new CoolArrayList<>();
    private long discardedRows = 0;

    void discardUnreachableRows() {
        final boolean[] blocked = new boolean[7];

        int discardableTopRow = 0;
        for (int i = chamber.size() - 1; i >= 0; i--) {
            final boolean[] newBlocked = new boolean[7];
            final char[] row = chamber.get(i);
            int nrBlocked = 0;

            for (int j = 0; j < row.length; j++) {
                if (row[j] != 0) newBlocked[j] = true;

                if (j == 0) {
                    if (blocked[0] && blocked[1]) newBlocked[0] = true;
                    if (blocked[0] && row[1] != 0) newBlocked[0] = true;
                } else if (j == row.length - 1) {
                    if (blocked[j] && blocked[j - 1]) newBlocked[j] = true;
                    if (blocked[j] && row[j - 1] != 0) newBlocked[j] = true;
                } else {
                    if (blocked[j] && (blocked[j - 1] || row[j - 1] != 0) && (blocked[j + 1] || row[j + 1] != 0))
                        newBlocked[j] = true;
                }

                if (newBlocked[j]) nrBlocked++;
            }

            if (nrBlocked == 7) {
                discardableTopRow = i;
                break;
            }

            System.arraycopy(newBlocked, 0, blocked, 0, blocked.length);
        }

        discardedRows += discardableTopRow;
        chamber.coolRemoveRange(0, discardableTopRow);
    }

    String stateCode() {
        return chamber.stream()
                .filter(row -> !Arrays.equals(row, EMPTY_ROW))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    void ensureHeight(final long height) {
        final int requiredSize = (int) (height - discardedRows);

        for (int i = chamber.size(); chamber.size() < requiredSize; i++) {
            chamber.add(new char[7]);
        }
    }

    long highestLevel() {
        for (int i = chamber.size() - 1; i >= 0; i--) {
            if (!Arrays.equals(chamber.get(i), EMPTY_ROW)) return discardedRows + i + 1;
        }
        return discardedRows;
    }

    char[] get(final long rowNr) {
        return chamber.get((int) (rowNr - discardedRows));
    }

    long bottom() {
        return discardedRows;
    }

    void raise(final long raiseRows) {
        discardedRows += raiseRows;
    }

    void dump() {
        final StringBuilder sb = new StringBuilder();

        for (int i = chamber.size() - 1; i >= 0; i--) {
            sb.append('|');
            for (final char c : chamber.get(i)) sb.append(c);
            sb.append("|\n");
        }

        sb.append("+-------+\n");
        sb.append("+ ").append(discardedRows).append(" rows\n");
        System.out.println(sb);
    }
}
