package com.sjoerdhemminga.adventofcode2022.day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star1 {
    private final String filename;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star1("input-own.txt").doFile();
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
            final int[] cipherText = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            final Node origHead = wrap(cipherText);
            //final Node origTail = origHead.circPrev;

            for (Node cur = origHead; cur != null; cur = cur.origNext) move(cur);

            final int[] mixed = unwrap(origHead, cipherText.length);

            if (mixed.length < 50) System.out.println("Arrays.toString(mixed) = " + Arrays.toString(mixed));

            final int zeroIdx = indexOf0(mixed);

            final int val1000 = mixed[(zeroIdx + 1000) % mixed.length];
            final int val2000 = mixed[(zeroIdx + 2000) % mixed.length];
            final int val3000 = mixed[(zeroIdx + 3000) % mixed.length];
            final int sum = val1000 + val2000 + val3000;

            System.out.println("val1000 = " + val1000);
            System.out.println("val2000 = " + val2000);
            System.out.println("val3000 = " + val3000);
            System.out.println("sum = " + sum);
        }

        System.out.println();
    }

    private static int indexOf0(final int[] mixed) {
        for (int i = 0; i < mixed.length; i++)
            if (mixed[i] == 0)
                return i;
        return -1;
    }

    private static int[] unwrap(final Node head, final int length) {
        final int[] mixed = new int[length];

        int i = 0;
        Node cur = head;

        do {
            mixed[i] = (int) cur.value;
            i++;
            cur = cur.circNext;
        } while (cur != head);

        return mixed;
    }

    private static Node wrap(final int[] list) {
        Node head = null;
        Node prev = null;

        for (final int nr : list) {
            final Node curNode = new Node(nr, prev);

            if (head == null) head = curNode;
            if (prev != null) prev.setOrigNext(curNode);

            prev = curNode;
        }

        // Link head and tail
        head.circPrev = prev;
        prev.circNext = head;
        return head;
    }

    private void move(final Node node) {
        if (node.value < 0) for (int i = 0; i < -node.value; i++) node.moveLeft();
        else if (node.value > 0) for (int i = 0; i < node.value; i++) node.moveRight();
    }
}
