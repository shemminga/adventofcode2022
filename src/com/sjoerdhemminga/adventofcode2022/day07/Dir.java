package com.sjoerdhemminga.adventofcode2022.day07;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

record Dir(Map<String, Dir> subdirs, Map<String, Long> files, Dir parent) {
    static Dir newDir(final Dir parent) {
        return new Dir(new HashMap<>(), new HashMap<>(), parent);
    }

    void addDir(final String name) {
        subdirs.put(name, newDir(this));
    }

    void addFile(final String name, final Long size) {
        files.put(name, size);
    }

    Dir cd(final String dir) {
        if ("..".equals(dir)) return  parent;
        if (!subdirs.containsKey(dir)) throw new AssertionError();
        return subdirs.get(dir);
    }

    Stream<Dir> listRecursive() {
        final Stream<Dir> subdirStream = subdirs.values()
                .stream()
                .flatMap(Dir::listRecursive);

        return Stream.concat(Stream.of(this), subdirStream);
    }

    long size() {
        return sizeFiles() + sizeSubdirs();
    }

    private long sizeFiles() {
        return files.values()
                .stream()
                .mapToLong(x -> x)
                .sum();
    }

    private long sizeSubdirs() {
        return subdirs.values()
                .stream()
                .mapToLong(Dir::size)
                .sum();
    }

}
