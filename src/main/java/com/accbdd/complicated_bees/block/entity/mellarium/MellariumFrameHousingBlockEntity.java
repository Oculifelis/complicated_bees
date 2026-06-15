package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.item.FrameItem;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MellariumFrameHousingBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private final ItemStackHandler frameItems;
    private final IItemHandler frameItemHandler;

    public MellariumFrameHousingBlockEntity(BlockPos pPos, BlockState pBlockState, int frameSlots) {
        super(BlockEntitiesRegistration.MELLARIUM_FRAME_HOUSING_ENTITIES.get(frameSlots-1).get(), pPos, pBlockState);
        frameItems = new ItemStackHandler(frameSlots) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem() instanceof FrameItem;
            }

            @Override
            protected void onContentsChanged(int slot) {
                if (getLogic() != null) {
                    getLogic().getController().ifPresent(controller -> controller.getLogic().checkConditions());
                }
                super.onContentsChanged(slot);
            }
        };
        frameItemHandler = new AdaptedItemHandler(frameItems);
    }

    public IItemHandler getFrameItemHandler() {
        return frameItemHandler;
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
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < frameItems.getSlots(); i++) {
                frameItems.getStackInSlot(i).hurtAndBreak(1, serverLevel, null, item -> {});
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        pTag.put("frame_items", frameItems.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.contains("frame_items"))
            frameItems.deserializeNBT(registries, pTag.getCompound("frame_items"));
    }
}
