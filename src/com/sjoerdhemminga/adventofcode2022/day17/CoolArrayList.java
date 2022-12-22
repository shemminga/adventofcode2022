package com.sjoerdhemminga.adventofcode2022.day17;

import java.util.ArrayList;

final class CoolArrayList<E> extends ArrayList<E> {
    // Open removeRange to other classes
    void coolRemoveRange(final int fromIndex, final int toIndex) {
        removeRange(fromIndex, toIndex);
    }
}
