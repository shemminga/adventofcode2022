package com.sjoerdhemminga.adventofcode2022.day21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static java.util.function.Predicate.not;

public final class Star1 {
    private static final Pattern COLON_SPACE = Pattern.compile(": ");
    private final String filename;
    private final Map<String, String> monkeyExpressions = new HashMap<>();
    private final Map<String, Long> monkeyResults = new HashMap<>();

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
            lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(COLON_SPACE::split)
                    .forEach(l -> {
                        if (l[1].charAt(0) >= '0' && l[1].charAt(0) <= '9')
                            monkeyResults.put(l[0], parseLong(l[1]));
                        else monkeyExpressions.put(l[0], l[1]);
                    });

            final long rootNumber = evaluateMonkey("root");

            System.out.println("rootNumber = " + rootNumber);
        }

        System.out.println();
    }

    private long evaluateMonkey(final String monkey) {
        if (monkeyResults.containsKey(monkey)) return monkeyResults.get(monkey);

        final String expr = monkeyExpressions.get(monkey);

        final String submonkey1 = expr.substring(0, 4);
        final String submonkey2 = expr.substring(7);

        final long operand1 = evaluateMonkey(submonkey1);
        final long operand2 = evaluateMonkey(submonkey2);

        final long result = switch (expr.charAt(5)) {
            case '+' -> operand1 + operand2;
            case '-' -> operand1 - operand2;
            case '*' -> operand1 * operand2;
            case '/' -> operand1 / operand2;
            default -> throw new AssertionError(expr.charAt(5));
        };

        monkeyResults.put(monkey, result);
        return result;
    }
}
