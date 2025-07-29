package com.accbdd.complicated_bees.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG_SPEC;
    public static ServerConfig SERVER_CONFIG;

    public final ForgeConfigSpec.ConfigValue<Integer> productionCycleLength;
    public final ForgeConfigSpec.ConfigValue<Integer> enviroCycleLength;
    public final ForgeConfigSpec.ConfigValue<Integer> centrifugeBaseSpeed;
    public final ForgeConfigSpec.ConfigValue<Integer> centrifugeBaseEnergy;
    public final ForgeConfigSpec.ConfigValue<Integer> furnaceGeneratorBaseEnergy;
    public final ForgeConfigSpec.ConfigValue<Integer> furnaceGeneratorBaseTransfer;
    public final ForgeConfigSpec.ConfigValue<Integer> furnaceGeneratorBaseStorage;
    public final ForgeConfigSpec.ConfigValue<Integer> honeyGeneratorBaseEnergy;
    public final ForgeConfigSpec.ConfigValue<Integer> honeyGeneratorBaseTransfer;
    public final ForgeConfigSpec.ConfigValue<Integer> honeyGeneratorBaseStorage;
    public final ForgeConfigSpec.ConfigValue<Integer> mellariumBaseTransfer;
    public final ForgeConfigSpec.ConfigValue<Integer> mellariumBaseStorage;
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
        furnaceGeneratorBaseEnergy = builder.comment("How much rf/tick a generator should produce while burning a fuel.").define("furnaceGeneratorBaseEnergy", 20);
        furnaceGeneratorBaseTransfer = builder.comment("How much rf/tick a generator should be able to transfer out of itself.").define("furnaceGeneratorBaseTransfer", 1000);
        furnaceGeneratorBaseStorage = builder.comment("How much rf a generator should be able to store.").define("furnaceGeneratorBaseStorage", 100000);
        honeyGeneratorBaseEnergy = builder.comment("How much rf/tick a honey generator should produce while burning a fuel.").define("honeyGeneratorBaseEnergy", 40);
        honeyGeneratorBaseTransfer = builder.comment("How much rf/tick a honey generator should be able to transfer out of itself.").define("honeyGeneratorBaseTransfer", 2000);
        honeyGeneratorBaseStorage = builder.comment("How much rf a honey generator should be able to store.").define("honeyGeneratorBaseStorage", 200000);
        mellariumBaseTransfer = builder.comment("How much rf/tick a mellarium should be able to transfer into itself.").define("mellariumBaseTransfer", 2000);
        mellariumBaseStorage = builder.comment("How much rf a mellarium should be able to store at base.").define("mellariumBaseStorage", 200000);
        builder.pop();
    }

    static {
        Pair<ServerConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);

        CONFIG_SPEC = pair.getRight();
        SERVER_CONFIG = pair.getLeft();
    }
}
