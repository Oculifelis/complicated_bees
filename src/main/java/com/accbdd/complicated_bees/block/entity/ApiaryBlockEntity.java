package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.BeeLogic;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Stack;

@ParametersAreNonnullByDefault
public class ApiaryBlockEntity extends BaseBeeHousing {
    BeeLogic beeLogic;

    private final Stack<ItemStack> outputBuffer = new Stack<>();
    private final ItemStackHandler beeItems = createBeeHandler();
    private final ItemStackHandler outputItems = createOutputHandler();
    private final ItemStackHandler frameItems = createFrameHandler();

    private final IItemHandlerModifiable beeItemHandler = new AdaptedItemHandler(beeItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    };

    private final IItemHandlerModifiable outputItemHandler = new AdaptedItemHandler(outputItems) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    };
    private final IItemHandlerModifiable frameItemHandler = new AdaptedItemHandler(frameItems) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof FrameItem;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    };

    private final IItemHandlerModifiable itemHandler = new CombinedInvWrapper(beeItemHandler, outputItemHandler);

    public ApiaryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.APIARY_ENTITY.get(), pPos, pBlockState);
        this.beeLogic = new BeeLogic(getLevel(), getBlockPos(), this);
    }

    public ItemStackHandler getBeeItems() {
        return beeItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public ItemStackHandler getFrameItems() {
        return frameItems;
    }

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return itemHandler;
    }

    @Override
    public IItemHandlerModifiable getBeeItemHandler() {
        return beeItemHandler;
    }

    @Override
    public IItemHandlerModifiable getOutputItemHandler() {
        return outputItemHandler;
    }

    @Override
    public IItemHandlerModifiable getFrameItemHandler() {
        return frameItemHandler;
    }

    public Stack<ItemStack> getOutputBuffer() {
        return outputBuffer;
    }

    @Override
    public BeeLogic getLogic() {
        return beeLogic;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        beeLogic.setLevel(getLevel());
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.OUTPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    private ItemStackHandler createFrameHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.FRAME_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                getLogic().checkConditions();
            }
        };
    }

    private ItemStackHandler createBeeHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.BEE_SLOT_COUNT) {
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
}
