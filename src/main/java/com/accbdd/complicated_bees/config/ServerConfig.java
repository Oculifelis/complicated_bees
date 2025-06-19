package com.accbdd.complicated_bees.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG_SPEC;
    public static ServerConfig SERVER_CONFIG;

    public final ForgeConfigSpec.ConfigValue<Integer> productionCycleLength, enviroCycleLength, centrifugeBaseSpeed, centrifugeBaseEnergy, generatorBaseEnergy, generatorBaseTransfer, generatorBaseStorage;
    public final ForgeConfigSpec.ConfigValue<Float> researchBonus;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("cycle_length");
        productionCycleLength = builder.comment("How long (in ticks) one bee cycle should take.").define("productionCycleLength", 200);
        enviroCycleLength = builder.comment("How long (in ticks) an apiary should wait between re-scanning the environment for appropriate conditions.").define("enviroCycleLength", 200);
        builder.pop();
        builder.push("research");
        researchBonus = builder.comment("The percentage bonus researching a mutation in the microscope should grant to that mutation's chances.").define("researchBonus", 0.2f);
        builder.pop();
        builder.push("rf");
        centrifugeBaseSpeed = builder.comment("How many ticks an unupgraded centrifuge should take to process a comb").define("centrifugeBaseSpeed", 200);
        centrifugeBaseEnergy = builder.comment("How much rf/tick an unupgraded centrifuge should use while processing a recipe.").define("centrifugeBaseEnergy", 20);
        generatorBaseEnergy = builder.comment("How much rf/tick a generator should produce while burning a fuel.").define("generatorBaseEnergy", 20);
        generatorBaseTransfer = builder.comment("How much rf/tick a generator should be able to transfer out of itself.").define("generatorBaseTransfer", 1000);
        generatorBaseStorage = builder.comment("How much rf/tick a generator should produce while burning a fuel.").define("generatorBaseStorage", 100000);
        builder.pop();
    }

    static {
        Pair<ServerConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);

        CONFIG_SPEC = pair.getRight();
        SERVER_CONFIG = pair.getLeft();
    }
}
