package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.item.FrameItem;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MellariumFrameHousingBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private final ItemStackHandler frameItems;
    private final LazyOptional<IItemHandler> frameItemHandler;

    public MellariumFrameHousingBlockEntity(BlockPos pPos, BlockState pBlockState, int frameSlots) {
        super(BlockEntitiesRegistration.MELLARIUM_FRAME_HOUSING_ENTITIES.get(frameSlots-1).get(), pPos, pBlockState);
        frameItems = new ItemStackHandler(frameSlots) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem() instanceof FrameItem;
            }
        };
        frameItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(frameItems));
    }

    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return frameItemHandler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.getFrameItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        getFrameItemHandler().invalidate();
    }

    public BeeHousingModifier getModifier() {
        List<BeeHousingModifier> modifiers = new ArrayList<>();
        for(int i = 0; i < frameItems.getSlots(); i++) {
            ItemStack stack = frameItems.getStackInSlot(i);
            if (stack.getItem() instanceof FrameItem frame) {
                modifiers.add(frame.getModifier());
            }
        }
        return BeeHousingModifier.of(modifiers.toArray(new BeeHousingModifier[0]));
    }

    @Override
    public void onBeeTick() {
        damageFrames();
    }

    public void damageFrames() {
        for (int i = 0; i < frameItems.getSlots(); i++) {
            if (frameItems.getStackInSlot(i).hurt(1, getLevel().random, null))
                frameItems.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        pTag.put("frame_items", frameItems.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.load(pTag, registries);
        if (pTag.contains("frame_items"))
            frameItems.deserializeNBT(registries, pTag.getCompound("frame_items"));
    }
}
