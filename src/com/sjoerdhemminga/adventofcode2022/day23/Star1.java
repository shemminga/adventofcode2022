package com.sjoerdhemminga.adventofcode2022.day23;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.sjoerdhemminga.adventofcode2022.day23.Direction.NORTH;
import static java.util.function.Predicate.not;

public final class Star1 {
    private final String filename;
    private List<Ij> elfs;
    private Direction considerDirection;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-5elfs.txt").doFile();
        new Star1("input-test.txt").doFile();
        new Star1("input.txt").doFile();
    }

    private Star1(final String filename) {
        this.filename = filename;
    }

    private void doFile() throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star1.class.getResource(filename);

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

            if (elfs.size() < 25) dump(-1);
            for (int round = 0; round < 10; round++) {
                doRound();
                if (elfs.size() < 25) dump(round);
            }

            final int emptyGroundTiles = emptyGroundTiles();
            System.out.println("emptyGroundTiles = " + emptyGroundTiles);
        }

        System.out.println();
    }

    private int emptyGroundTiles() {
        final Ij minB = Ij.minBound(elfs);
        final Ij maxB = Ij.maxBound(elfs);

        final int sizeI = maxB.i() - minB.i() + 1;
        final int sizeJ = maxB.j() - minB.j() + 1;

        final int surface = sizeI * sizeJ;

        System.out.print("minB = " + minB);
        System.out.println("; maxB = " + maxB);
        System.out.printf("sizeI: %2d  sizeJ: %2d  surface: %4d  elfs.size(): %2d%n",
                sizeI, sizeJ, surface, elfs.size());

        return surface - elfs.size();
    }

    private void dump(final int round) {
        System.out.printf("After round %2d%n", round);

        final Ij minB = Ij.minBound(elfs);
        final Ij maxB = Ij.maxBound(elfs);

        System.out.print("minB = " + minB);
        System.out.println("; maxB = " + maxB);

        final char[][] grid = new char[maxB.i() - minB.i() + 1][maxB.j() - minB.j() + 1];
        for (final char[] row : grid) Arrays.fill(row, '.');

        elfs.forEach(elf -> grid[elf.i() - minB.i()][elf.j() - minB.j()] = '#');

        final StringBuilder sb = new StringBuilder();
        for (final char[] chars : grid)
            sb.append(chars)
                    .append('\n');

        sb.append("Next consider direction: ").append(considerDirection).append('\n');

        System.out.println(sb);
    }

    private void doRound() {
        final Set<Ij> taken = new HashSet<>(elfs);
        final Map<Ij, Ij> proposed = new HashMap<>();
        final Map<Ij, List<Ij>> reverseProposed = new HashMap<>();

        for (final Ij elf: elfs) {
            if (!containsAny(taken, elf.getAllNeighbours())) continue;

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
    }

    private static boolean containsAny(final Set<Ij> haystack, final Set<Ij> needles) {
        for (final Ij needle : needles)
            if (haystack.contains(needle))
                return true;
        return false;
    }
}
