package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeeSorterBlockEntity extends BlockEntity {
    public static final String TYPES = "types";
    private byte[] typeFilters;
    public BeeSorterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.BEE_SORTER_BLOCK_ENTITY.get(), pPos, pBlockState);
        typeFilters = new byte[]{4,4,4,4,4,4};
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putByteArray(TYPES, typeFilters);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(TYPES))
            typeFilters = pTag.getByteArray(TYPES);
    }

    public void setTypeFilters(byte[] typeFilters) {
        this.typeFilters = typeFilters;
        setChanged();
    }

    public void setTypeFilters(int index, byte value) {
        this.typeFilters[index] = value;
        setChanged();
    }

    public byte[] getTypeFilters() {
        return typeFilters;
    }
}
