package com.accbdd.complicated_bees.recipe.mutation.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class NighttimeCondition extends MutationCondition {
    public static String ID = "nighttime";

    public NighttimeCondition() {

    }

    @Override
    public Identifier getID() {
        return Identifier.fromNamespaceAndPath(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        return !level.isBrightOutside();
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
