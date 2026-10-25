package com.accbdd.complicated_bees.compat.jade;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.ApiaryBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

/** Jade requires data providers to remain separate from client component providers. */
public final class ApiaryDataProvider implements IServerDataProvider<BlockAccessor> {
    public static final ApiaryDataProvider INSTANCE = new ApiaryDataProvider();

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof ApiaryBlockEntity apiary) {
            tag.putInt("errors", apiary.getErrors());
            tag.putInt("cycle_ticks", apiary.getCycleProgress());
        }
    }

    @Override
    public Identifier getUid() {
        return Identifier.fromNamespaceAndPath(ComplicatedBees.MODID, "apiary_errors");
    }
}
