package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MicroscopeBlockEntity extends BlockEntity {
    public static final String ITEMS_TAG = "items";
    private final ItemStackHandler items = createItemHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items));
    private boolean locked = false;

    public MicroscopeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistration.MICROSCOPE_BLOCK_ENTITY.get(), pos, state);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(6) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? stack.is(ItemTagGenerator.BEE) : stack.is(ItemTagGenerator.RESEARCH_MATERIAL);
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
        }
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public void markDirty() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
