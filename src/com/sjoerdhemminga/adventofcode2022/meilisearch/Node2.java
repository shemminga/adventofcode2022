package com.sjoerdhemminga.adventofcode2022.meilisearch;

import java.util.ArrayList;
import java.util.List;

final class Node2 {
    private final List<String> names = new ArrayList<>();
    private Node2 l, r, parent;
    private String fullPath, pathPart;
    private boolean visited;

    Node2(final Node2 parent, final String fullPath, final String pathPart) {
        this.parent = parent;
        this.fullPath = fullPath;
        this.pathPart = pathPart;
    }

    Node2 ensureL() {
        if (l == null) l = new Node2(this, fullPath + 'L', "L");
        return l;
    }

    Node2 ensureR() {
        if (r == null) r = new Node2(this, fullPath + 'R', "R");
        return r;
    }

    void addName(final String name) {
        names.add(name);
    }

    Node2 getL() {
        return l;
    }

    Node2 getR() {
        return r;
    }

    Node2 getParent() {
        return parent;
    }

    List<String> getNames() {
        return names;
    }

    boolean hasNames() {
        return !names.isEmpty();
    }

    String getPathPart() {
        return pathPart;
    }

    String getFullPath() {
        return fullPath;
    }

    boolean isVisited() {
        return visited;
    }

    void setVisited(final boolean visited) {
        this.visited = visited;
    }

    void mergeIfNeeded() {
        if (hasNames()) return;
        if (l != null && r != null) return;
        if (l == null && r == null) throw new AssertionError("Makes no sense");

        if (l != null) {
            fullPath += l.fullPath;
            pathPart += l.pathPart;
            names.addAll(l.names);
            r = l.r;
            l = l.l;
        } else if (r != null) {
            fullPath += r.fullPath;
            pathPart += r.pathPart;
            names.addAll(r.names);
            l = r.l;
            r = r.r;
        }

        if (l != null) l.parent = this;
        if (r != null) r.parent = this;
    }
}
