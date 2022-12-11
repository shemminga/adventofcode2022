package com.sjoerdhemminga.adventofcode2022.day11;

import java.util.Deque;
import java.util.function.Function;

record Monkey(Deque<Long> items,
              Function<Long, Long> operation,
              long divisableByTest,
              int trueTarget,
              int falseTarget) {

    boolean hasItemToThrow() {
        return !items.isEmpty();
    }

    long getItemReadyToThrowStar1() {
        Long item = items.removeFirst();
        item = operation.apply(item);
        item /= 3;
        return item;
    }

    long getItemReadyToThrowStar2(final long maxModulo) {
        Long item = items.removeFirst();
        item = operation.apply(item);
        item %= maxModulo;
        return item;
    }

    int getTarget(final long i) {
        return (i % divisableByTest == 0) ? trueTarget : falseTarget;
    }
}
