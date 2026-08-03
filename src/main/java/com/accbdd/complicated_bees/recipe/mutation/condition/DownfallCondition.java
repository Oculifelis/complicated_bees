package com.accbdd.complicated_bees.recipe.mutation.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class DownfallCondition extends MutationCondition {
    public static String ID = "downfall";

    public DownfallCondition() {

    }

    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        return level.isRainingAt(pos);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("gui.complicated_bees.mutations.downfall");
    }

    @Override
    public CompoundTag serialize() {
        return new CompoundTag();
    }

    @Override
    public DownfallCondition deserialize(CompoundTag tag) {
        return new DownfallCondition();
    }
}
