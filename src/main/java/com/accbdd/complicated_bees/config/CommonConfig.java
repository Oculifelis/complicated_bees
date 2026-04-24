package com.accbdd.complicated_bees.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {
    public static ModConfigSpec CONFIG_SPEC;
    public static CommonConfig COMMON_CONFIG;

    public final ModConfigSpec.ConfigValue<Boolean> frame, waxedFrame, honeyFrame, twistingFrame, soothingFrame, coldFrame, hotFrame, dryFrame, wetFrame, deadlyFrame, restrictiveFrame;
    public final ModConfigSpec.ConfigValue<Boolean> honeyBread, honeyPorkchop, ambrosia;
    public final ModConfigSpec.ConfigValue<Boolean> beeStaff;

    CommonConfig(ModConfigSpec.Builder builder) {
        builder.push("items");
        builder.push("frames");
        frame = builder.comment("Enable the basic frame").define("frameEnabled", true);
        waxedFrame = builder.comment("Enable the waxed frame").define("waxedFrameEnabled", true);
        honeyFrame = builder.comment("Enable the honeyed frame").define("honeyFrameEnabled", true);
        twistingFrame = builder.comment("Enable the twisting frame").define("twistingFrameEnabled", true);
        soothingFrame = builder.comment("Enable the soothing frame").define("soothingFrameEnabled", true);
        coldFrame = builder.comment("Enable the cold frame").define("coldFrameEnabled", true);
        hotFrame = builder.comment("Enable the hot frame").define("hotFrameEnabled", true);
        dryFrame = builder.comment("Enable the dry frame").define("dryFrameEnabled", true);
        wetFrame = builder.comment("Enable the wet frame").define("wetFrameEnabled", true);
        deadlyFrame = builder.comment("Enable the deadly frame").define("deadlyFrameEnabled", true);
        restrictiveFrame = builder.comment("Enable the restrictive frame").define("restrictiveFrameEnabled", true);
        builder.pop();
        builder.push("foods");
        honeyBread = builder.comment("Enable honey bread").define("honeyBreadEnabled", true);
        honeyPorkchop = builder.comment("Enable honey porkchop").define("honeyPorkchopEnabled", true);
        ambrosia = builder.comment("Enable ambrosia").define("ambrosiaEnabled", true);
        builder.pop();
        builder.push("misc");
        beeStaff = builder.comment("Enable bee staff").define("staffEnabled", true);
        builder.pop(2);

        //todo: add config option for inheritance? i.e. which genes are tied to species
    }

    static {
        Pair<CommonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CommonConfig::new);

        CONFIG_SPEC = pair.getRight();
        COMMON_CONFIG = pair.getLeft();
    }
}
