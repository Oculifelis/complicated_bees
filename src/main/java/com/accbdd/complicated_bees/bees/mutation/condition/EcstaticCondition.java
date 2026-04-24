package com.accbdd.complicated_bees.bees.mutation.condition;

import com.accbdd.complicated_bees.bees.IBeeHousing;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class EcstaticCondition extends MutationCondition {
    public static String ID = "ecstatic";

    public EcstaticCondition() {

    }

    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof IBeeHousing housing) {
            return housing.isQueenEcstatic();
        }
        return false;
    }

    @Override
    public Component getDescription() {
        return Component.translatable("gui.complicated_bees.mutations.ecstatic");
    }

    @Override
    public CompoundTag serialize() {
        return new CompoundTag();
    }

    @Override
    public EcstaticCondition deserialize(CompoundTag tag) {
        return new EcstaticCondition();
    }
}
