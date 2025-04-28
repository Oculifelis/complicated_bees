package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BeeSorterBlockEntity extends BlockEntity {
    public static final String TYPES = "types";
    private byte[] typeFilters; //down, up, north, south, east, west
    private final ItemStackHandler item;
    private final List<LazyOptional<IItemHandler>> handlers;

    public BeeSorterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.BEE_SORTER_BLOCK_ENTITY.get(), pPos, pBlockState);
        typeFilters = new byte[]{4,4,4,4,4,4};
        item = new ItemStackHandler(1);
        handlers = new ArrayList<>();
        for (int i = 0; i < typeFilters.length; i++) {
            handlers.add(createFilterHandler(i));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return side == null ? handlers.get(0).cast() : handlers.get(side.ordinal()).cast();
        return super.getCapability(cap, side);
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

    private boolean testFilter(int index, ItemStack stack) {
        return switch (typeFilters[index]) {
            case 0 -> stack.is(ItemsRegistration.DRONE.get());
            case 1 -> stack.is(ItemsRegistration.PRINCESS.get());
            case 2 -> stack.is(ItemsRegistration.QUEEN.get());
            case 3 -> stack.is(ItemTagGenerator.BEE);
            default -> !stack.is(ItemTagGenerator.BEE);
        };
    }

    private LazyOptional<IItemHandler> createFilterHandler(int index) {
        return LazyOptional.of(() -> new AdaptedItemHandler(item) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return testFilter(index, getStackInSlot(0)) ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
            }
        });
    }

    public ItemStackHandler getItem() {
        return item;
    }
}
