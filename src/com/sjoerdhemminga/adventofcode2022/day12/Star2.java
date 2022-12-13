package com.sjoerdhemminga.adventofcode2022.day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;

public final class Star2 {
    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2().doFile("input-test.txt");
        new Star2().doFile("input.txt");
    }

    private void doFile(final String filename) throws IOException, URISyntaxException {
        System.out.println("*** input file: " + filename + " ***");
        final URL input = Star2.class.getResource(filename);

        try (final Stream<String> lines = Files.lines(Paths.get(input.toURI()))) {
            final char[][] grid = lines.filter(not(String::isBlank))
                    .map(String::toCharArray)
                    .toList()
                    .toArray(char[][]::new);

            final List<int[]> starts = new ArrayList<>();

            int[] end = {-1, -1};
            for (int i = 0; i < grid.length; i++)
                for (int j = 0; j < grid[i].length; j++)
                    if (grid[i][j] == 'S' || grid[i][j] == 'a') {
                        grid[i][j] = 'a';
                        starts.add(new int[]{i, j});
                    } else if (grid[i][j] == 'E') {
                        grid[i][j] = 'z';
                        end = new int[]{i, j};
                    }

            final int[] finalEnd = end; // Stupid

            final int minEndDist = starts.stream()
                    .mapToInt(start -> calcEndDist(grid, start, finalEnd))
                    .min()
                    .orElseThrow();

            System.out.println("minEndDist = " + minEndDist);
        }

        System.out.println();
    }

    private int calcEndDist(final char[][] grid, final int[] start, final int[] end) {
        final int[][] distances = new int[grid.length][grid[0].length];
        for (final int[] distance : distances) Arrays.fill(distance, Integer.MAX_VALUE);
        distances[start[0]][start[1]] = 0;

        final PriorityQueue<int[]> pq = new PriorityQueue<>(comparingInt(x -> distances[x[0]][x[1]]));
        pq.add(start);

        dijkstra(grid, distances, pq);

        return distances[end[0]][end[1]];
    }

    private void dijkstra(final char[][] grid, final int[][] distances, final PriorityQueue<int[]> pq) {
        while (!pq.isEmpty()) {
            //System.out.println("pq = " + pq);

            final int[] cur = pq.remove();
            final int curI = cur[0];
            final int curJ = cur[1];

            testAndAdd(grid, distances, pq, curI, curJ, curI - 1, curJ);
            testAndAdd(grid, distances, pq, curI, curJ, curI + 1, curJ);
            testAndAdd(grid, distances, pq, curI, curJ, curI, curJ - 1);
            testAndAdd(grid, distances, pq, curI, curJ, curI, curJ + 1);
        }
    }

    private static void testAndAdd(final char[][] grid, final int[][] distances, final PriorityQueue<int[]> pq,
            final int curI, final int curJ, final int nextI, final int nextJ) {
        if (nextI < 0 || nextI >= grid.length) return;
        if (nextJ < 0 || nextJ >= grid[0].length) return;

        final char curCell = grid[curI][curJ];
        final int curDist = distances[curI][curJ];

        //System.out.printf("curCell: %c\tcurDist: %d\tnextCell: %c\tnextDist: %d",
        //        curCell, curDist, grid[nextI][nextJ], distances[nextI][nextJ]);

        if (curCell + 1 >= grid[nextI][nextJ]) {
            if (curDist + 1 < distances[nextI][nextJ]) {
                //System.out.print(" -> Added");
                distances[nextI][nextJ] = curDist + 1;
                final int[] nextCoords = {nextI, nextJ};
                pq.removeIf(coords -> Arrays.equals(coords, nextCoords));
                pq.add(nextCoords);
            }
        }
        //System.out.println();
    }
}
