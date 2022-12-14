package com.sjoerdhemminga.adventofcode2022.day14;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern ARROW = Pattern.compile(" -> ");
    private static final Ij START_IJ = new Ij(500, 0);
    private final String filename;

    public static void main(final String... args) throws IOException, URISyntaxException {
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
            final List<Segment> segments = lines.filter(not(String::isBlank))
                    .map(ARROW::split)
                    .flatMap(this::segmentize)
                    .map(Segment::parse)
                    .toList();

            final List<Ij> ijs = segments
                    .stream()
                    .flatMap(Segment::coords)
                    .toList();

            final Ij minIj = Ij.minBound(ijs);
            final Ij maxIj = Ij.maxBound(ijs);

            final Grid grid = new Grid(maxIj);

            grid.set(START_IJ, '+');
            segments.stream()
                    .flatMap(s -> s.start().streamTo(s.end()))
                    .forEach(ij -> grid.set(ij, '#'));

            //System.out.println("=== Before ===");
            //System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));

            int count = 0;
            while(dropSand(grid, minIj, maxIj)) {
                count++;

                //if (List.of(1,2,5,22,24).contains(count)) {
                //    System.out.println("=== After " + count + " ===");
                //    System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));
                //}
            }

            //System.out.println("=== After ===");
            //System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));

            System.out.println("count = " + count);
        }

        System.out.println();
    }

    private boolean dropSand(final Grid grid, final Ij minIj, final Ij maxIj) {
        Ij curIj = START_IJ;

        while (true) {
            Ij nextIj = curIj.withJ(curIj.j() + 1);
            if (outOfBounds(nextIj, minIj, maxIj)) return false;
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            nextIj = curIj.withJ(curIj.j() + 1).withI(curIj.i() - 1);
            if (outOfBounds(nextIj, minIj, maxIj)) return false;
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            nextIj = curIj.withJ(curIj.j() + 1).withI(curIj.i() + 1);
            if (outOfBounds(nextIj, minIj, maxIj)) return false;
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            grid.set(curIj, 'o');
            return true;
        }
    }

    private boolean outOfBounds(final Ij nextIj, final Ij minIj, final Ij maxIj) {
        return (nextIj.j() > maxIj.j()) || (nextIj.i() < minIj.i()) || (nextIj.i() > maxIj.i());
    }

    private Stream<String[]> segmentize(final String[] strings) {
        final List<String[]> segments = new ArrayList<>(strings.length - 1);

        for (int i = 1; i < strings.length; i++)
            segments.add(new String[]{strings[i - 1], strings[i]});

        return segments.stream();
    }
}
