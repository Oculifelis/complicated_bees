package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.item.FrameItem;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MellariumFrameHousingBlockEntity extends MellariumAbstractBlockEntity {
    public static int FRAME_SLOTS = 2;

    private final ItemStackHandler frameItems = new ItemStackHandler(FRAME_SLOTS);
    private final LazyOptional<IItemHandler> frameItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(frameItems) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof FrameItem;
        }
    });

    public MellariumFrameHousingBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_FAN_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return frameItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }
}
