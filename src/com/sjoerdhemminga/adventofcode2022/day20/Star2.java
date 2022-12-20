package com.sjoerdhemminga.adventofcode2022.day20;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public final class Star2 {
    private static final long DECRYPTION_KEY = 811_589_153;

    private final String filename;

    public static void main(final String... args) throws IOException, URISyntaxException {
        new Star2("input-own.txt").doFile();
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
            final long[] cipherText = lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .mapToLong(Long::parseLong)
                    .map(x -> x * DECRYPTION_KEY)
                    .toArray();

            final Node origHead = wrap(cipherText);
            //final Node origTail = origHead.circPrev;

            //System.out.println("Before rounds : " + Arrays.toString(cipherText));

            for (int i = 0; i < 10; i++) {
                for (Node cur = origHead; cur != null; cur = cur.origNext)
                    move(cur, cipherText.length);

                //final long[] mixed = unwrap(origHead, cipherText.length);
                //System.out.printf("After round %2d: %s%n", i, Arrays.toString(mixed));
            }

            final long[] mixed = unwrap(origHead, cipherText.length);

            if (mixed.length < 50) System.out.println("Arrays.toString(mixed) = " + Arrays.toString(mixed));

            final int zeroIdx = indexOf0(mixed);

            final long val1000 = mixed[(zeroIdx + 1000) % mixed.length];
            final long val2000 = mixed[(zeroIdx + 2000) % mixed.length];
            final long val3000 = mixed[(zeroIdx + 3000) % mixed.length];
            final long sum = val1000 + val2000 + val3000;

            System.out.println("val1000 = " + val1000);
            System.out.println("val2000 = " + val2000);
            System.out.println("val3000 = " + val3000);
            System.out.println("sum = " + sum);
        }

        System.out.println();
    }

    private static int indexOf0(final long[] mixed) {
        for (int i = 0; i < mixed.length; i++)
            if (mixed[i] == 0)
                return i;
        return -1;
    }

    private static long[] unwrap(final Node head, final int length) {
        final long[] mixed = new long[length];

        int i = 0;
        Node cur = head;

        do {
            mixed[i] = cur.value;
            i++;
            cur = cur.circNext;
        } while (cur != head);

        return mixed;
    }

    private static Node wrap(final long[] list) {
        Node head = null;
        Node prev = null;

        for (final long nr : list) {
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

    private void move(final Node node, final int length) {
        final long value = node.value % (length - 1);
        if (value < 0) for (int i = 0; i < -value; i++) node.moveLeft();
        else if (value > 0) for (int i = 0; i < value; i++) node.moveRight();
    }
}
