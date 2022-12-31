package com.sjoerdhemminga.adventofcode2022.day19;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

record Blueprint(int blueprintNumber, int oreOre, int clayOre, int obsidianOre, int obsidianClay, int geodeOre,
                 int geodeObsidian) {
    private static final Pattern PATTERN = Pattern.compile("^Blueprint (\\d+): " +
            "Each ore robot costs (\\d+) ore. " +
            "Each clay robot costs (\\d+) ore. " +
            "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
            "Each geode robot costs (\\d+) ore and (\\d+) obsidian.$");

    static Blueprint parse(final String line) {
        final Matcher matcher = PATTERN.matcher(line.trim());
        if (!matcher.matches()) throw new AssertionError(line);

        final int blueprintNumber = parseInt(matcher.group(1));
        final int oreOre = parseInt(matcher.group(2));
        final int clayOre = parseInt(matcher.group(3));
        final int obsidianOre = parseInt(matcher.group(4));
        final int obsidianClay = parseInt(matcher.group(5));
        final int geodeOre = parseInt(matcher.group(6));
        final int geodeObsidian = parseInt(matcher.group(7));

        return new Blueprint(blueprintNumber, oreOre, clayOre, obsidianOre, obsidianClay, geodeOre, geodeObsidian);
    }
}
