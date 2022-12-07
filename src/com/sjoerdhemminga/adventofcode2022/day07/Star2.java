package com.sjoerdhemminga.adventofcode2022.day07;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final long TOTAL_DISK_SIZE = 70_000_000;
    private static final long REQUIRED_FREE_SIZE = 30_000_000;

    private final Dir root = Dir.newDir(null);
    private Dir cur = root;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2().doFile("input-test.txt");
        new Star2().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .skip(1) // Always "$ cd /"
                    .forEach(this::parse);
        }

        final long usedSpace = root.size();
        System.out.println("usedSpace = " + usedSpace);

        final long sizeToFind = REQUIRED_FREE_SIZE - (TOTAL_DISK_SIZE - usedSpace);
        System.out.println("sizeToFind = " + sizeToFind);

        final long dirToRemoveSize = root.listRecursive()
                .mapToLong(Dir::size)
                .filter(size -> size >= sizeToFind)
                .min()
                .orElseThrow();

        System.out.println("dirToRemoveSize = " + dirToRemoveSize);

        System.out.println();
    }

    private void parse(final String line) {
        switch (line.charAt(0)) {
        case '$' -> parseCmd(line);
        case 'd' -> cur.addDir(line.substring(4));
        default -> {
            final int sep = line.indexOf(' ');
            final long size = Long.parseLong(line.substring(0, sep));
            final String name = line.substring(sep + 1);
            cur.addFile(name, size);
        }
        }
    }

    private void parseCmd(final String line) {
        switch (line.charAt(2)) {
        case 'c' -> cur = cur.cd(line.substring(5));
        case 'l' -> {} // Don't need to do anything.
        default -> throw new AssertionError();
        }
    }
}
