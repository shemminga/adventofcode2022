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

public final class Star2 {
    private static final Pattern ARROW = Pattern.compile(" -> ");
    private static final Ij START_IJ = new Ij(500, 0);
    private final String filename;

    public static void main(final String... args) throws IOException, URISyntaxException {
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
            final List<Segment> segments = lines.filter(not(String::isBlank))
                    .map(ARROW::split)
                    .flatMap(this::segmentize)
                    .map(Segment::parse)
                    .toList();

            final List<Ij> ijs = segments
                    .stream()
                    .flatMap(Segment::coords)
                    .toList();

            final Ij oldMinIj = Ij.minBound(ijs);
            final Ij oldMaxIj = Ij.maxBound(ijs);

            final int bottomJ = oldMaxIj.j() + 2;
            final int bottomMinI = oldMinIj.i() - oldMaxIj.j();
            final int bottomMaxI = oldMaxIj.i() + oldMaxIj.j();

            final Segment bottom = new Segment(new Ij(bottomMinI, bottomJ), new Ij(bottomMaxI, bottomJ));

            final Ij minIj = new Ij(bottomMinI, oldMinIj.j());
            final Ij maxIj = bottom.end();

            final Grid grid = new Grid(bottom.end());

            grid.set(START_IJ, '+');
            Stream.concat(Stream.of(bottom), segments.stream())
                    .flatMap(s -> s.start().streamTo(s.end()))
                    .forEach(ij -> grid.set(ij, '#'));

            System.out.println("=== Before ===");
            System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));

            int count = 0;
            while(dropSand(grid)) {
                count++;

                //if (List.of(1,2,5,22,24).contains(count)) {
                //    System.out.println("=== After " + count + " ===");
                //    System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));
                //}
            }

            System.out.println("=== After ===");
            System.out.println(grid.blockToString(new Ij(minIj.i(), 0), maxIj));

            System.out.println("count = " + count);
        }

        System.out.println();
    }

    private boolean dropSand(final Grid grid) {
        Ij curIj = START_IJ;

        if (grid.get(curIj) != '+') return false;

        while (true) {
            Ij nextIj = curIj.withJ(curIj.j() + 1);
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            nextIj = curIj.withJ(curIj.j() + 1).withI(curIj.i() - 1);
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            nextIj = curIj.withJ(curIj.j() + 1).withI(curIj.i() + 1);
            if (grid.get(nextIj) == 0) {
                curIj = nextIj;
                continue;
            }

            grid.set(curIj, 'o');
            return true;
        }
    }

    private Stream<String[]> segmentize(final String[] strings) {
        final List<String[]> segments = new ArrayList<>(strings.length - 1);

        for (int i = 1; i < strings.length; i++)
            segments.add(new String[]{strings[i - 1], strings[i]});

        return segments.stream();
    }
}
