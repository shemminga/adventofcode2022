package com.sjoerdhemminga.adventofcode2022.day20;

import static java.lang.String.format;

final class Node {
    final long value;
    final Node origPrev;
    Node origNext;
    Node circPrev;
    Node circNext;

    Node(final long value, final Node origPrev) {
        this.value = value;
        this.origPrev = origPrev;
        circPrev = origPrev;
    }

    void setOrigNext(final Node origNext) {
        this.origNext = origNext;
        circNext = origNext;
    }

    void moveLeft() {
        moveCircLeft();
    }

    private void moveCircLeft() {
        circNext.circPrev = circPrev;
        circPrev.circNext = circNext;

        circPrev.circPrev.circNext = this;
        circNext = circPrev;

        final Node origCircPrev2 = circPrev.circPrev;
        circPrev.circPrev = this;
        circPrev = origCircPrev2;
    }

    void moveRight() {
        moveCircRight();
    }

    private void moveCircRight() {
        circPrev.circNext = circNext;
        circNext.circPrev = circPrev;
        circNext.circNext.circPrev = this;
        circPrev = circNext;

        final Node origCircNext2 = circNext.circNext;
        circNext.circNext = this;
        circNext = origCircNext2;
    }

    @Override
    public String toString() {
        return format(" [%2d %s, %s] ", value,
                ptrPairToString("orig", origPrev, origNext),
                ptrPairToString("circ", circPrev, circNext));
    }

    private String ptrPairToString(final String pairName, final Node ptrPrev, final Node ptrNext) {
        return format("%s=%s;%s", pairName, ptrToString(ptrPrev), ptrToString(ptrNext));
    }

    private String ptrToString(final Node ptr) {
        if (ptr == null) return "NN";
        return format("%2d", ptr.value);
    }
}
