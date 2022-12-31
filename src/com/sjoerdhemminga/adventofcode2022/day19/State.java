package com.sjoerdhemminga.adventofcode2022.day19;

record State(Blueprint blueprint, int timeLeft, int oreCount, int clayCount, int obsidianCount, int geodeCount,
             int oreProd, int clayProd, int obsidianProd, int geodeProd) {
    boolean extraGeodeProdIsUseful() {
        return timeLeft > 1;
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

    State nextWithoutAction() {
        return new State(blueprint,
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

    State nextWithExtraOreProd() {
        return new State(blueprint,
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

    State nextWithExtraClayProd() {
        return new State(blueprint,
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

    State nextWithExtraObsidianProd() {
        return new State(blueprint,
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

    State nextWithExtraGeodeProd() {
        return new State(blueprint,
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
