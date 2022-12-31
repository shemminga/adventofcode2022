package com.sjoerdhemminga.adventofcode2022.day19;

record State2(Blueprint blueprint, int timeLeft, int oreCount, int clayCount, int obsidianCount, int geodeCount,
              int oreProd, int clayProd, int obsidianProd, int geodeProd) {
    boolean extraGeodeProdIsUseful() {
        return timeLeft > geodeProdIsUsefulTime();
    }

    private static int geodeProdIsUsefulTime() {
        return 1;
    }

    boolean extraObsidianProdIsUseful() {
        return obsidianProd < blueprint.geodeObsidian() && timeLeft > obsidianProdIsUsefulTime();
    }

    private int obsidianProdIsUsefulTime() {
        final int buildPoint = geodeProdIsUsefulTime() + 1; // Build geode at 2 (> 1)
        final int earlier = 1; // Need prod 1 earlier

        return buildPoint + earlier;
    }

    boolean extraClayProdIsUseful() {
        return clayProd < blueprint.obsidianClay() && timeLeft > clayProdIsUsefulTime();
    }

    private int clayProdIsUsefulTime() {
        final int buildPoint = obsidianProdIsUsefulTime() + 1; // Build obs
        final int earlier = 1; // Need prod 1 earlier

        return buildPoint + earlier;
    }

    boolean extraOreProdIsUseful() {
        return (oreProd < blueprint.oreOre() || oreProd < blueprint.clayOre() || oreProd < blueprint.obsidianOre() ||
                oreProd < blueprint.geodeOre()) && timeLeft > oreProdIsUsefulTime();
    }

    private int oreProdIsUsefulTime() {
        final int buildPoint = geodeProdIsUsefulTime() + 1; // Build geode at 2 (> 1)
        final int earlier = 1; // Need prod 1 earlier

        return buildPoint + earlier;
    }

    boolean canBuyExtraOreProd() {
        return oreCount >= blueprint.oreOre();
    }

    boolean canBuyExtraClayProd() {
        return oreCount >= blueprint.clayOre();
    }

    boolean canBuyExtraObsidianProd() {
        return oreCount >= blueprint.obsidianOre() && clayCount >= blueprint.obsidianClay();
    }

    boolean canBuyExtraGeodeProd() {
        return oreCount >= blueprint.geodeOre() && obsidianCount >= blueprint().geodeObsidian();
    }

    State2 nextWithoutAction() {
        return new State2(blueprint,
                timeLeft - 1,
                oreCount + oreProd,
                clayCount + clayProd,
                obsidianCount + obsidianProd,
                geodeCount + geodeProd,
                oreProd,
                clayProd,
                obsidianProd,
                geodeProd);
    }

    State2 nextWithExtraOreProd() {
        return new State2(blueprint,
                timeLeft - 1,
                oreCount + oreProd - blueprint.oreOre(),
                clayCount + clayProd,
                obsidianCount + obsidianProd,
                geodeCount + geodeProd,
                oreProd + 1,
                clayProd,
                obsidianProd,
                geodeProd);
    }

    State2 nextWithExtraClayProd() {
        return new State2(blueprint,
                timeLeft - 1,
                oreCount + oreProd - blueprint.clayOre(),
                clayCount + clayProd,
                obsidianCount + obsidianProd,
                geodeCount + geodeProd,
                oreProd,
                clayProd + 1,
                obsidianProd,
                geodeProd);
    }

    State2 nextWithExtraObsidianProd() {
        return new State2(blueprint,
                timeLeft - 1,
                oreCount + oreProd - blueprint.obsidianOre(),
                clayCount + clayProd - blueprint.obsidianClay(),
                obsidianCount + obsidianProd,
                geodeCount + geodeProd,
                oreProd,
                clayProd,
                obsidianProd + 1,
                geodeProd);
    }

    State2 nextWithExtraGeodeProd() {
        return new State2(blueprint,
                timeLeft - 1,
                oreCount + oreProd - blueprint.geodeOre(),
                clayCount + clayProd,
                obsidianCount + obsidianProd - blueprint.geodeObsidian(),
                geodeCount + geodeProd,
                oreProd,
                clayProd,
                obsidianProd,
                geodeProd + 1);
    }
}
