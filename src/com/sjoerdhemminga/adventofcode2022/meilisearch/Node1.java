package com.sjoerdhemminga.adventofcode2022.meilisearch;

import java.util.ArrayList;
import java.util.List;

final class Node1 {
    private Node1 l, r;
    private List<String> names = new ArrayList<>();
    private int dist;

    Node1 ensureL() {
        if (l == null) l = new Node1();
        return l;
    }

    Node1 ensureR() {
        if (r == null) r = new Node1();
        return r;
    }

    void addName(final String name) {
        names.add(name);
    }

    Node1 getL() {
        return l;
    }

    Node1 getR() {
        return r;
    }

    List<String> getNames() {
        return names;
    }

    int getDist() {
        return dist;
    }

    void setDist(final int dist) {
        this.dist = dist;
    }
}
