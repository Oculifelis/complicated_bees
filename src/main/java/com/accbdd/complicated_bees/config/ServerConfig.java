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
    public final ForgeConfigSpec.ConfigValue<Integer> mellariumCellTransfer;
    public final ForgeConfigSpec.ConfigValue<Integer> mellariumCellStorage;
    public final ForgeConfigSpec.ConfigValue<Integer> gyrofugeCellTransfer;
    public final ForgeConfigSpec.ConfigValue<Integer> gyrofugeCellStorage;
    public final ForgeConfigSpec.ConfigValue<Integer> gyrofugeBaseSpeed;
    public final ForgeConfigSpec.ConfigValue<Integer> gyrofugeBaseUsage;
    public final ForgeConfigSpec.ConfigValue<Integer> gyrofugeBaseIdleUsage;
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
        mellariumCellTransfer = builder.comment("How much rf/tick a mellarium energy cell should be able to transfer into itself.").define("mellariumCellTransfer", 2000);
        mellariumCellStorage = builder.comment("How much rf a mellarium energy cell should be able to store at base.").define("mellariumCellStorage", 200000);
        gyrofugeCellTransfer = builder.comment("How much rf/tick a gyrofuge energy cell should be able to transfer into itself.").define("gyrofugeCellTransfer", 2000);
        gyrofugeCellStorage = builder.comment("How much rf a gyrofuge energy cell should be able to store at base.").define("gyrofugeCellStorage", 200000);
        gyrofugeBaseSpeed = builder.comment("How many ticks an unupgraded gyrofuge should take to process a comb").define("gyrofugeBaseSpeed", 100);
        gyrofugeBaseUsage = builder.comment("How much rf/tick an unupgraded gyrofuge should use while processing a recipe.").define("gyrofugeBaseUsage", 50);
        gyrofugeBaseIdleUsage = builder.comment("How much rf/tick an unupgraded gyrofuge should use while idle.").define("gyrofugeBaseIdleUsage", 10);
        builder.pop();
    }

    static {
        Pair<ServerConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);

        CONFIG_SPEC = pair.getRight();
        SERVER_CONFIG = pair.getLeft();
    }
}
