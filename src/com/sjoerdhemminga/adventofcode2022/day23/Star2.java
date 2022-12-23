package com.sjoerdhemminga.adventofcode2022.day23;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2022.day23.Direction.NORTH;
import static java.util.function.Predicate.not;

public final class Star2 {
    private final String filename;
    private List<Ij> elfs;
    private Direction considerDirection;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-5elfs.txt").doFile();
        new Star2("input-test.txt").doFile();
        new Star2("input.txt").doFile();
    }

    private Star2(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(String::toCharArray)
                    .toArray(char[][]::new);

            elfs = new ArrayList<>();
            for (int i = 0; i < grid.length; i++)
                for (int j = 0; j < grid[0].length; j++)
                    if (grid[i][j] == '#')
                        elfs.add(new Ij(i, j));

            considerDirection = NORTH;

            int round = 0;
            do round++; while (doRound());

            System.out.println("round = " + round);
        }

        System.out.println();
    }

    private boolean doRound() {
        final Set<Ij> taken = new HashSet<>(elfs);
        final Map<Ij, Ij> proposed = new HashMap<>();
        final Map<Ij, List<Ij>> reverseProposed = new HashMap<>();

        boolean elfMoved = false;
        for (final Ij elf: elfs) {
            if (!containsAny(taken, elf.getAllNeighbours())) continue;
            elfMoved = true;

            Direction proposedDir = considerDirection;

            boolean canMove = true;
            while (containsAny(taken, elf.getDirection(proposedDir))) {
                proposedDir = proposedDir.next();
                if (proposedDir == considerDirection) {
                    canMove = false;
                    break;
                }
            }

            if (canMove) {
                final Ij propMove = elf.move(proposedDir);
                proposed.put(elf, propMove);
                reverseProposed.computeIfAbsent(propMove, k -> new ArrayList<>())
                        .add(elf);
            }
        }

        considerDirection = considerDirection.next();

        reverseProposed.values()
                .stream()
                .filter(c -> c.size() > 1)
                .flatMap(Collection::stream)
                .forEach(proposed::remove);

        elfs = elfs.stream()
                .map(e -> proposed.getOrDefault(e, e))
                .toList();

        return elfMoved;
    }

    private static boolean containsAny(final Set<Ij> haystack, final Set<Ij> needles) {
        for (final Ij needle : needles)
            if (haystack.contains(needle))
                return true;
        return false;
    }
}
