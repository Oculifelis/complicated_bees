package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.block.entity.ApiaryBlockEntity;
import com.accbdd.complicated_bees.block.entity.IBeeHousing;
import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.genetics.BeeHousingModifier;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.effect.IBeeEffect;
import com.accbdd.complicated_bees.genetics.gene.GeneEffect;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class MellariumLogic implements IBeeHousing {
    private final ItemStackHandler beeItems = createBeeHandler();
    private final ItemStackHandler outputItems = createOutputHandler();
    private final ItemStackHandler frameItems = createFrameHandler();

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(beeItems, outputItems, frameItems));
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

    private final Level level;
    private final BlockPos center;
    private UUID owner;

    public MellariumLogic(Level level, BlockPos center, UUID owner) {
        this.level = level;
        this.center = center;
        this.owner = owner;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(this);
            } else {
                ComplicatedBees.LOGGER.error("tried to build a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("built new mellarium with center {}", center);
    }

    public void deconstruct() {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(null);
            } else {
                ComplicatedBees.LOGGER.error("tried to deconstruct a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("deconstructed mellarium with center {}", center);
    }

    public BlockPos getCenter() {
        return center;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getBeeItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return null;
    }

    @Override
    public void doBeeEffect() {
        if (beeItems.getStackInSlot(0).getItem() instanceof QueenItem ) {
            IBeeEffect effect = (IBeeEffect) GeneticHelper.getGeneValue(beeItems.getStackInSlot(0), GeneEffect.ID, true);
            if (effect != null)
                effect.runEffect(level.getBlockEntity(center), beeItems.getStackInSlot(0), cycleProgress);
        }
    }

    @Override
    public List<BeeHousingModifier> getHousingModifiers() {
        return null;
    }

    @Override
    public void addToOutput(ItemStack stack) {

    }

    @Override
    public boolean isQueenSatisfied() {
        return false;
    }

    @Override
    public boolean isQueenEcstatic() {
        return false;
    }

    @Override
    public int getErrors() {
        return 0;
    }

    @Override
    public void beeTick() {

    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.OUTPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                //setChanged();
            }
        };
    }

    private ItemStackHandler createFrameHandler() {
        return new ItemStackHandler(ApiaryBlockEntity.FRAME_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                //setChanged();
                humidityCache = null;
                temperatureCache = null;
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
                    ;
                    checkQueenSatisfied();
                }
                //setChanged();
            }
        };
    }


}
