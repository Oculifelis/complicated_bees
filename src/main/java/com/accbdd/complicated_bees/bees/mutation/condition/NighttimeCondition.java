package com.accbdd.complicated_bees.bees.mutation.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class NighttimeCondition extends MutationCondition {
    public static String ID = "nighttime";

    public NighttimeCondition() {

    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        return level.isNight();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("gui.complicated_bees.mutations.nighttime");
    }

    @Override
    public CompoundTag serialize() {
        return new CompoundTag();
    }

    @Override
    public NighttimeCondition deserialize(CompoundTag tag) {
        return new NighttimeCondition();
    }
}
