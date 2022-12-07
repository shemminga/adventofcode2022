package com.sjoerdhemminga.adventofcode2022.day07;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private final Dir root = Dir.newDir(null);
    private Dir cur = root;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1().doFile("input-test.txt");
        new Star1().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            lines.filter(not(String::isBlank))
                    .skip(1) // Always "$ cd /"
                    .forEach(this::parse);
        }

        System.out.println("root.size() = " + root.size());

        final long smallDirSize = root.listRecursive()
                .mapToLong(Dir::size)
                .filter(size -> size <= 100_000)
                .sum();

        System.out.println("smallDirSize = " + smallDirSize);

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
