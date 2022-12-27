package com.sjoerdhemminga.adventofcode2022.day16;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

record Valve(String label, int flowRate, List<String> connectedValves) {
    private static final Pattern COMMA = Pattern.compile(", ");

    static Valve parse(final String line) {
        final String label = line.substring(6, 8);

        final int scIdx = line.indexOf(';', 23);
        final int flowRate = parseInt(line.substring(23, scIdx));

        final int startOfList = line.charAt(scIdx + 23) == 's' ? scIdx + 25 : scIdx + 24;
        final String[] connected = COMMA.split(line.substring(startOfList));

        return new Valve(label, flowRate, new ArrayList<>(List.of(connected)));
    }
}
