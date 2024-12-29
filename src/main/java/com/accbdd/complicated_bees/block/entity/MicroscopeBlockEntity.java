package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MicroscopeBlockEntity extends BaseContainerBlockEntity {
    public NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private final ItemStackHandler itemHandler = createItemHandler(items);

    public MicroscopeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistration.MICROSCOPE_BLOCK_ENTITY.get(), pos, state);
    }

    private ItemStackHandler createItemHandler(NonNullList<ItemStack> items) {
        return new ItemStackHandler(items) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(ItemTagGenerator.BEE);
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                //todo: blockrenderer not updating to empty until opened again
                if(!level.isClientSide())
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.complicated_bees.microscope");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return items.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return itemHandler.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        itemHandler.setStackInSlot(index, stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
