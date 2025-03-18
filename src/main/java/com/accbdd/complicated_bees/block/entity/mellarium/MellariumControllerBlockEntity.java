package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.bees.BeeLogic;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.block.entity.BaseBeeHousing;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class MellariumControllerBlockEntity extends BaseBeeHousing {
    public static final int BEE_SLOT = 0;
    public static final int BEE_SLOT_COUNT = 2;
    public static final String ITEMS_BEES_TAG = "bee_items";

    public static final int OUTPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_COUNT = 7;
    public static final String ITEMS_OUTPUT_TAG = "output_items";

    public static final int SLOT_COUNT = BEE_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    public static final String OUTPUT_BUFFER_TAG = "output_buffer";

    private MellariumLogic mellariumLogic;
    BeeLogic beeLogic;

    private final Stack<ItemStack> outputBuffer = new Stack<>();
    private final ItemStackHandler beeItems = createBeeHandler();
    private final ItemStackHandler outputItems = createOutputHandler();
    private final ItemStackHandler frameItems = new ItemStackHandler(0);

    private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(beeItems, outputItems));
    private final LazyOptional<IItemHandlerModifiable> beeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(beeItems) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });

    private final LazyOptional<IItemHandlerModifiable> outputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    });

    private final LazyOptional<IItemHandlerModifiable> frameItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(frameItems) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof FrameItem;
        }
    });

    public MellariumControllerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState, null);
    }

    public MellariumControllerBlockEntity(BlockPos pPos, BlockState pBlockState, MellariumLogic mellariumLogic) {
        super(BlockEntitiesRegistration.MELLARIUM_CONTROLLER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.mellariumLogic = mellariumLogic;
        if (mellariumLogic != null) {
            this.setOwner(mellariumLogic.getOwner());
        }
        this.beeLogic = new BeeLogic(getLevel(), getBlockPos(), this);
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(OUTPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    private ItemStackHandler createBeeHandler() {
        return new ItemStackHandler(BEE_SLOT_COUNT) {
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
    public LazyOptional<IItemHandlerModifiable> getItemHandler() {
        return itemHandler;
    }

    @Override
    public LazyOptional<IItemHandlerModifiable> getBeeItemHandler() {
        return beeItemHandler;
    }

    @Override
    public LazyOptional<IItemHandlerModifiable> getOutputItemHandler() {
        return outputItemHandler;
    }

    @Override
    public LazyOptional<IItemHandlerModifiable> getFrameItemHandler() {
        return LazyOptional.empty();
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
        if (mellariumLogic != null)
            mellariumLogic.setOwner(owner);
    }

    @Override
    public void tickServer() {
        super.tickServer();
    }

    @Override
    public void generateProduce(ItemStack bee) {
        super.generateProduce(bee);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (mellariumLogic == null && MultiblockHelper.isValidMellarium(getLevel(), getBlockPos())) {
            MultiblockHelper.buildMellarium(getLevel(), getBlockPos(), getOwner());
        }
        getLogic().setPos(getLogic().getPos().above());
        setChanged();
    }

    public MellariumLogic getMellariumLogic() {
        if (mellariumLogic == null && MultiblockHelper.isValidMellarium(getLevel(), getBlockPos())) {
            MultiblockHelper.buildMellarium(getLevel(), getBlockPos(), getOwner());
        }
        return mellariumLogic;
    }

    public void setMellariumLogic(MellariumLogic logic) {
        this.mellariumLogic = logic;
    }

    @Override
    public void damageFrames() {
        for (BlockPos pos : getMellariumLogic().getSpecialBlocks()) {
            if (getLevel().getBlockEntity(pos) instanceof MellariumFrameHousingBlockEntity frameHousing) {
                frameHousing.damageFrames();
            }
        }
    }

    @Override
    public List<BeeHousingModifier> getHousingModifiers() {
        if (getMellariumLogic() == null)
            return List.of();
        return getMellariumLogic().getSpecialBlocks().stream().map(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumModifier modifier) {
                return modifier.getModifier();
            }
            return new BeeHousingModifier();
        }).toList();
    }
}
