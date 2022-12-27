package com.sjoerdhemminga.adventofcode2022.day16;

record Mover(String pos, int timeToOpen, boolean moving) {
    static final Mover STOPPED_MOVER = new Mover("", -1, false);
    static final Mover START_MOVER = new Mover("AA", 0, true);

    Mover safeSubtractTime(final int timeToNextOpen) {
        if (!moving) return this;
        return subtractTime(timeToNextOpen);
    }

    Mover subtractTime(final int timeToNextOpen) {
        if (!moving) throw new AssertionError(moving);
        if (timeToOpen - timeToNextOpen < 0) throw new AssertionError(timeToOpen + " - " + timeToNextOpen);
        if (pos == null || pos.length() != 2) throw new AssertionError(pos);

        return new Mover(pos, timeToOpen - timeToNextOpen, moving);
    }

    Mover withNewPos(final String newPos, final int newTimeToOpen) {
        final Mover newMover = new Mover(newPos, newTimeToOpen, moving);
        newMover.assertState();
        return newMover;
    }

    void assertState() {
        if (this != STOPPED_MOVER) {
            if (!moving) throw new AssertionError(moving);
            if (timeToOpen < 0) throw new AssertionError(timeToOpen);
            if (pos == null || pos.length() != 2) throw new AssertionError(pos);
        }
    }
}
