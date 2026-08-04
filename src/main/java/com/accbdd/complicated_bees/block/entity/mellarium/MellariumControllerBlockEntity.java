package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.bees.BeeLogic;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.block.entity.BaseBeeHousing;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.item.DroneItem;
import com.accbdd.complicated_bees.item.PrincessItem;
import com.accbdd.complicated_bees.item.QueenItem;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MellariumControllerBlockEntity extends BaseBeeHousing {
    public static final int BEE_SLOT = 0;
    public static final int BEE_SLOT_COUNT = 2;
    public static final int OUTPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_COUNT = 7;
    public static final int SLOT_COUNT = BEE_SLOT_COUNT + OUTPUT_SLOT_COUNT;

    private MellariumLogic mellariumLogic;
    BeeLogic beeLogic;

    private final Stack<ItemStack> outputBuffer = new Stack<>();
    private final ItemStackHandler beeItems = createBeeHandler();
    private final ItemStackHandler outputItems = createOutputHandler();
    private final ItemStackHandler frameItems = new ItemStackHandler(0);


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

    private final IItemHandlerModifiable itemHandler = new CombinedInvWrapper(beeItemHandler, outputItemHandler);

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
        return null;
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
        if (getMellariumLogic() == null)
            return;
        getMellariumLogic().getSpecialBlocks().stream().forEach(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumTickable tickable) {
                tickable.onTick();
            }
        });
    }

    @Override
    public void beeTick() {
        super.beeTick();
        getMellariumLogic().getSpecialBlocks().stream().forEach(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumTickable tickable) {
                tickable.onBeeTick();
            }
        });
    }

    @Override
    public void produceOffspring(ItemStack queen, BlockPos pos) {
        super.produceOffspring(queen, getBlockPos().below()); //get block pos below the center, not the center itself
        getMellariumLogic().getSpecialBlocks().stream().forEach(specialPos -> {
            if (getLevel().getBlockEntity(specialPos) instanceof IMellariumTickable tickable) {
                tickable.onDeath();
            }
        });
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
        Map<Item, Integer> frameMap = new HashMap<>();
        getMellariumLogic().getSpecialBlocks().stream().forEach(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumFrameHolder frameHolder) {
                frameHolder.getFrames().stream().forEach(stack -> {
                    frameMap.computeIfAbsent(stack.getItem(), k -> 0);
                    frameMap.put(stack.getItem(), frameMap.get(stack.getItem()) + 1);
                });
            }
        });

        // damage each frame by modified square of duplicates
        getMellariumLogic().getSpecialBlocks().stream().forEach(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumFrameHolder frameHolder) {
                frameMap.forEach((item, damageAmount) -> frameHolder.damageFrames(item, (int) Math.max(1, damageAmount * damageAmount / 2)));
            }
        });
    }

    @Override
    public List<BeeHousingModifier> getHousingModifiers() {
        if (getMellariumLogic() == null)
            return List.of();
        List<BeeHousingModifier> list = getMellariumLogic().getSpecialBlocks().stream().map(pos -> {
            if (getLevel().getBlockEntity(pos) instanceof IMellariumModifier modifier) {
                return modifier.getModifier();
            }
            return new BeeHousingModifier();
        }).collect(Collectors.toList());
        list.add(new BeeHousingModifier.Builder().productivity(1.25f).build());
        return list;
    }

    public IEnergyStorage getEnergyHandler() {
        return getMellariumLogic().getEnergyStorage();
    }
}
