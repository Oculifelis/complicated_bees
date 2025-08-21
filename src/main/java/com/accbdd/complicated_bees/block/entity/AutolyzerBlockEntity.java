package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
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

public class AutolyzerBlockEntity extends BlockEntity {
    public static final String ITEMS_TAG = "items";
    public static final int SLOT_COUNT = 2;
    public static final int SLOT = 0;
    private final ItemStackHandler items;
    private final LazyOptional<IItemHandler> itemHandler;

    public AutolyzerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.AUTOLYZER_BLOCK_ENTITY.get(), pPos, pBlockState);
        items = createItemHandler();
        itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot == 1 && isBeeAnalyzed(getStackInSlot(1)))
                    return super.extractItem(slot, amount, simulate);
                else
                    return ItemStack.EMPTY;
            }
        });
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return getItemHandler().cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ITEMS_TAG, items.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(ITEMS_TAG))
            items.deserializeNBT(pTag.getCompound(ITEMS_TAG));
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                ItemStack bee = getStackInSlot(1);
                if (!getStackInSlot(0).isEmpty()) {
                    if (!bee.isEmpty()) {
                        if (!isBeeAnalyzed(bee)) {
                            bee.getOrCreateTag().putBoolean(BeeItem.ANALYZED_TAG, true);
                            getStackInSlot(0).shrink(1);
                        }
                    }
                }
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == 0)
                    return stack.is(ItemTagGenerator.ANALYZER_FUEL);
                if (slot == 1)
                    return stack.is(ItemTagGenerator.BEE);
                return true;
            }
        };
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public boolean isBeeAnalyzed(ItemStack bee) {
        return bee.is(ItemTagGenerator.BEE) && bee.getOrCreateTag().getBoolean(BeeItem.ANALYZED_TAG);
    }
}
