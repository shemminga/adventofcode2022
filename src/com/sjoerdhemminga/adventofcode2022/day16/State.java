package com.sjoerdhemminga.adventofcode2022.day16;

import java.util.List;

import static com.sjoerdhemminga.adventofcode2022.day16.Mover.STOPPED_MOVER;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;

record State(Mover human, Mover elephant, List<String> valvesLeft, int flowRate, int timeLeft) {
    boolean allStopped() {
        return !human.moving() && !elephant.moving();
    }

    Mover arriver() {
        if (human.moving() && human.timeToOpen() == 0) return human;
        if (elephant.moving() && elephant.timeToOpen() == 0) return elephant;
        throw new AssertionError(human + " " + elephant);
    }

    Mover nonArriver() {
        if (arriver() == human) return elephant;
        return human;
    }

    State travelTo(final String valve, final int travelTimeToOpen, final int newFlowRate) {
        if (allStopped()) throw new AssertionError(this);

        final List<String> newValvesLeft = valvesLeft.stream()
                .filter(not(valve::equals))
                .toList();

        final int timeToNextOpen =
                nonArriver().moving() ? min(travelTimeToOpen, nonArriver().timeToOpen()) : travelTimeToOpen;

        final Mover newHuman, newElephant;
        if (arriver() == human) {
            newHuman = human.withNewPos(valve, travelTimeToOpen).subtractTime(timeToNextOpen);
            newElephant = elephant.safeSubtractTime(timeToNextOpen);
        } else {
            newHuman = human.safeSubtractTime(timeToNextOpen);
            newElephant = elephant.withNewPos(valve, travelTimeToOpen).subtractTime(timeToNextOpen);
        }

        final int newTimeLeft = timeLeft - timeToNextOpen;

        final State newState = new State(newHuman, newElephant, newValvesLeft, newFlowRate, newTimeLeft);
        newState.assertState();
        return newState;
    }

    State stopMoving(final int newFlowRate) {
        if (!nonArriver().moving()) {
            // Both stopped
            return new State(STOPPED_MOVER, STOPPED_MOVER, valvesLeft, newFlowRate, timeLeft);
        }

        final int timeToNextOpen = nonArriver().timeToOpen(); // Arriver is stopped, so only nonArriver matters

        final Mover newHuman = arriver() == human ? STOPPED_MOVER : human.subtractTime(timeToNextOpen);
        final Mover newElephant = arriver() == elephant ? STOPPED_MOVER : elephant.subtractTime(timeToNextOpen);

        final int newTimeLeft = timeLeft - timeToNextOpen;

        final State newState = new State(newHuman, newElephant, valvesLeft, newFlowRate, newTimeLeft);
        newState.assertState();
        return newState;
    }

    void assertState() {
        if (timeLeft <= 0) throw new AssertionError(timeLeft);
        if (flowRate < 0) throw new AssertionError(flowRate);
        human.assertState();
        elephant.assertState();

        if (!allStopped()) {
            if (human.timeToOpen() != 0 && elephant.timeToOpen() != 0) throw new AssertionError(this);
        }
    }
}
