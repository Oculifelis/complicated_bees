package com.accbdd.complicated_bees.compat.jade;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.block.entity.BeeNestBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeNestComponentProvider implements IBlockComponentProvider {
    public static BeeNestComponentProvider INSTANCE = new BeeNestComponentProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(blockAccessor.getBlockEntity() instanceof BeeNestBlockEntity nest ? GeneticHelper.getTranslationKey(nest.getSpecies()) : Component.literal("INVALID SPECIES"));
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "bee_nest_type");
    }
}
