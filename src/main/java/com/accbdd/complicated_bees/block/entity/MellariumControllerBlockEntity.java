package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.BeeLogic;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;
import java.util.UUID;

public class MellariumControllerBlockEntity extends BaseBeeHousing {
    private MellariumLogic mellariumLogic;
    BeeLogic beeLogic;

    private final Stack<ItemStack> outputBuffer = new Stack<>();
    private final ItemStackHandler beeItems = createBeeHandler();
    private final ItemStackHandler outputItems = createOutputHandler();
    private final ItemStackHandler frameItems = new ItemStackHandler();

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(beeItems, outputItems));
    private final LazyOptional<IItemHandler> beeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(beeItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });

    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    });

    private final LazyOptional<IItemHandler> frameItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(frameItems) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof FrameItem;
        }
    });

    public MellariumControllerBlockEntity(BlockPos pPos, BlockState pBlockState, MellariumLogic mellariumLogic) {
        super(BlockEntitiesRegistration.MELLARIUM_CONTROLLER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.mellariumLogic = mellariumLogic;
        if (mellariumLogic != null) {
            this.setOwner(mellariumLogic.getOwner());
        }
        this.beeLogic = new BeeLogic(getLevel(), getBlockPos(), this);
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.OUTPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    private ItemStackHandler createBeeHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.BEE_SLOT_COUNT) {
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                boolean itemValid = isItemValid(slot, stack);
                return itemValid ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (stack.getItem() instanceof BeeItem) {
                    switch (slot) {
                        case 0:
                            return (stack.getItem() instanceof QueenItem || stack.getItem() instanceof PrincessItem);
                        case 1:
                            return (stack.getItem() instanceof DroneItem);
                    }
                }
                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if (slot == 0) {
                    getLogic().setQueen(getStackInSlot(0));
                    getLogic().clearFlowerCache();
                    getLogic().checkConditions();
                }
                setChanged();
            }
        };
    }

    @Override
    public ItemStackHandler getBeeItems() {
        return beeItems;
    }

    @Override
    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    @Override
    public ItemStackHandler getFrameItems() {
        return frameItems;
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getBeeItemHandler() {
        return beeItemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return frameItemHandler;
    }

    @Override
    public Stack<ItemStack> getOutputBuffer() {
        return outputBuffer;
    }

    @Override
    public BeeLogic getLogic() {
        return beeLogic;
    }

    @Override
    public void setOwner(UUID owner) {
        super.setOwner(owner);
        if (getMellariumLogic() != null)
            getMellariumLogic().setOwner(owner);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (mellariumLogic == null && MultiblockHelper.isValidMellarium(getLevel(), getBlockPos())) {
            MultiblockHelper.buildMellarium(getLevel(), getBlockPos(), getOwner());
        }
    }

    public MellariumLogic getMellariumLogic() {
        return mellariumLogic;
    }

    public void setMellariumLogic(MellariumLogic logic) {
        this.mellariumLogic = logic;
    }
}
