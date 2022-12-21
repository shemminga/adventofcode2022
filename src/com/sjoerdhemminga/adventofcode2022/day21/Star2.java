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

public final class Star2 {
    private static final Pattern COLON_SPACE = Pattern.compile(": ");
    private final String filename;
    private final Map<String, String> monkeyExpressions = new HashMap<>();
    private final Map<String, Long> monkeyResults = new HashMap<>();

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
            lines.filter(not(String::isBlank))
                    .map(String::trim)
                    .map(COLON_SPACE::split)
                    .forEach(l -> {
                        if (l[1].charAt(0) >= '0' && l[1].charAt(0) <= '9')
                            monkeyResults.put(l[0], parseLong(l[1]));
                        else monkeyExpressions.put(l[0], l[1]);
                    });

            monkeyResults.remove("humn");

            evaluateMonkey("root");
            solve("root");

            final Long humnYell = monkeyResults.get("humn");

            System.out.println("humnYell = " + humnYell);
        }

        System.out.println();
    }

    private void solve(final String monkey) {
        final String expr = monkeyExpressions.get(monkey);
        final String submonkey1 = expr.substring(0, 4);
        final String submonkey2 = expr.substring(7);

        final boolean operand1Known = monkeyResults.containsKey(submonkey1);
        final long knownOperand = monkeyResults.get(operand1Known ? submonkey1 : submonkey2);
        final String unknownMonkey = operand1Known ? submonkey2 : submonkey1;

        solve(unknownMonkey, knownOperand);
    }

    private void solve(final String monkey, final long targetValue) {
        if ("humn".equals(monkey)) {
            monkeyResults.put(monkey, targetValue);
            return;
        }

        final String expr = monkeyExpressions.get(monkey);
        final String submonkey1 = expr.substring(0, 4);
        final String submonkey2 = expr.substring(7);
        final char operator = expr.charAt(5);

        final boolean operand1Known = monkeyResults.containsKey(submonkey1);
        final long knownOperand = monkeyResults.get(operand1Known ? submonkey1 : submonkey2);
        final String unknownMonkey = operand1Known ? submonkey2 : submonkey1;

        switch (operator) {
        case '+' -> solve(unknownMonkey, targetValue - knownOperand);
        case '-' -> solve(unknownMonkey, operand1Known ? (knownOperand - targetValue) : (knownOperand + targetValue));
        case '*' -> solve(unknownMonkey, targetValue / knownOperand);
        case '/' -> solve(unknownMonkey, operand1Known ? (knownOperand / targetValue) : (knownOperand * targetValue));
        default -> throw new AssertionError(operator);
        }
    }

    private boolean evaluateMonkey(final String monkey) {
        if (monkeyResults.containsKey(monkey)) return true;

        if ("humn".equals(monkey)) return false;

        final String expr = monkeyExpressions.get(monkey);
        final String submonkey1 = expr.substring(0, 4);
        final String submonkey2 = expr.substring(7);
        final char operator = expr.charAt(5);

        final boolean evaluatable1 = evaluateMonkey(submonkey1);
        final boolean evaluatable2 = evaluateMonkey(submonkey2);

        if (!evaluatable1 || !evaluatable2) return false;

        final long operand1 = monkeyResults.get(submonkey1);
        final long operand2 = monkeyResults.get(submonkey2);

        if ("root".equals(monkey)) return false;

        final long result = switch (operator) {
            case '+' -> operand1 + operand2;
            case '-' -> operand1 - operand2;
            case '*' -> operand1 * operand2;
            case '/' -> operand1 / operand2;
            default -> throw new AssertionError(operator);
        };

        monkeyResults.put(monkey, result);
        return true;
    }
}
