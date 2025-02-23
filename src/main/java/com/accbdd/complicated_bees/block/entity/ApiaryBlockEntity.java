package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.config.Config;
import com.accbdd.complicated_bees.genetics.BeeHousingModifier;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Product;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.effect.IBeeEffect;
import com.accbdd.complicated_bees.genetics.gene.*;
import com.accbdd.complicated_bees.genetics.gene.enums.EnumLifespan;
import com.accbdd.complicated_bees.genetics.gene.enums.EnumProductivity;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ApiaryBlockEntity extends BlockEntity implements IBeeHousing {
    public static final int BEE_SLOT = 0;
    public static final int BEE_SLOT_COUNT = 2;
    public static final String ITEMS_BEES_TAG = "bee_items";

    public static final int OUTPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_COUNT = 7;
    public static final String ITEMS_OUTPUT_TAG = "output_items";

    public static final int FRAME_SLOT = 0;
    public static final int FRAME_SLOT_COUNT = 3;
    public static final String FRAME_SLOT_TAG = "frame_slots";

    public static final int SLOT_COUNT = BEE_SLOT_COUNT + OUTPUT_SLOT_COUNT + FRAME_SLOT_COUNT;

    public final Stack<ItemStack> outputBuffer = new Stack<>();
    public static final String OUTPUT_BUFFER_TAG = "output_buffer";

    public static final int CYCLE_LENGTH = Config.CONFIG.productionCycleLength.get();
    public static final String CYCLE_TAG = "cycle";
    public static final int SATISFY_CYCLE_LENGTH = Config.CONFIG.enviroCycleLength.get();

    public static final String OWNER_TAG = "owner";
    private UUID owner = null;

    private final ContainerData data;
    private int cycleProgress = 0;
    private int satisfyCycleProgress = 0;
    private int matingProgress = 0;
    private int maxMatingProgress = 20;
    private int errorState = 0;

    private BeeLogic beeLogic;

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

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        beeItemHandler.invalidate();
        outputItemHandler.invalidate();
        frameItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.getItemHandler().cast();
            }
            if (side == Direction.DOWN) {
                return this.getOutputItemHandler().cast();
            }
            return this.getBeeItemHandler().cast();
        }
        return super.getCapability(cap, side);
    }

    public ApiaryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.APIARY_ENTITY.get(), pPos, pBlockState);
        this.beeLogic = new BeeLogic(getLevel(), getBlockPos(), this);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ApiaryBlockEntity.this.matingProgress;
                    case 1 -> ApiaryBlockEntity.this.maxMatingProgress;
                    case 2 -> ApiaryBlockEntity.this.errorState;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ApiaryBlockEntity.this.matingProgress = value;
                    case 1 -> ApiaryBlockEntity.this.maxMatingProgress = value;
                    case 2 -> ApiaryBlockEntity.this.errorState = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
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

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<IItemHandler> getBeeItemHandler() {
        return beeItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return frameItemHandler;
    }

    public ContainerData getData() {
        return this.data;
    }

    public int getCycleProgress() {
        return CYCLE_LENGTH - this.cycleProgress;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Nullable
    public UUID getOwner() {
        return owner;
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
                beeLogic.clearConditionCache();
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
                    beeLogic.setQueen(getStackInSlot(0));
                    beeLogic.clearFlowerCache();
                    beeLogic.checkConditions();
                }
                setChanged();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(CYCLE_TAG, IntTag.valueOf(cycleProgress));
        tag.put(ITEMS_BEES_TAG, beeItems.serializeNBT());
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT());
        tag.put(FRAME_SLOT_TAG, frameItems.serializeNBT());
        if (owner != null)
            tag.putUUID(OWNER_TAG, owner);
        ListTag bufferTag = new ListTag();
        for (ItemStack stack : outputBuffer) {
            bufferTag.add(stack.save(new CompoundTag()));
        }
        tag.put(OUTPUT_BUFFER_TAG, bufferTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        cycleProgress = tag.getInt(CYCLE_TAG);
        if (tag.contains(ITEMS_BEES_TAG)) {
            beeItems.deserializeNBT(tag.getCompound(ITEMS_BEES_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        if (tag.contains(FRAME_SLOT_TAG)) {
            frameItems.deserializeNBT(tag.getCompound(FRAME_SLOT_TAG));
        }
        if (tag.contains(OUTPUT_BUFFER_TAG)) {
            for (Tag itemCompound : tag.getList(OUTPUT_BUFFER_TAG, Tag.TAG_COMPOUND)) {
                outputBuffer.add(ItemStack.of((CompoundTag) itemCompound));
            }
        }
        if (tag.contains(OWNER_TAG))
            owner = tag.getUUID(OWNER_TAG);
        satisfyCycleProgress = new Random().nextInt(0, SATISFY_CYCLE_LENGTH);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        beeLogic.setLevel(getLevel());
    }

    public void tickServer() {
        ItemStack top_stack = beeItems.getStackInSlot(0);
        ItemStack bottom_stack = beeItems.getStackInSlot(1);

        //empty buffer
        if (!outputBuffer.empty()) {
            tryEmptyBuffer();
        }

        //mate
        if (top_stack.getItem() instanceof PrincessItem && bottom_stack.getItem() instanceof DroneItem) {
            increaseMatingProgress();
            if (hasFinished()) {
                resetMatingProgress();
                beeItems.extractItem(1, 1, false);
                beeItems.setStackInSlot(0, createQueenFromPrincessAndDrone(top_stack, bottom_stack));
                beeLogic.setLevel(getLevel());
                beeLogic.rebuildFlowerCache();
                beeLogic.checkConditions();
            }
        } else {
            resetMatingProgress();
        }

        //check if queen is satisfied
        if (satisfyCycleProgress >= SATISFY_CYCLE_LENGTH) {
            if (top_stack.getItem() instanceof QueenItem) {
                beeLogic.setQueen(top_stack);
                beeLogic.checkConditions();
                satisfyCycleProgress = 0;
            }
        } else {
            satisfyCycleProgress++;
        }

        //do queen cycle
        if (top_stack.getItem() instanceof QueenItem) {
            if (beeLogic.isQueenSatisfied()) {
                doBeeEffect();
                if (cycleProgress < CYCLE_LENGTH) {
                    cycleProgress++;
                } else {
                    cycleProgress = 0;
                    beeTick();
                }
            }
        } else {
            cycleProgress = 0;
        }
    }

    public void doBeeEffect() {
        if (beeItems.getStackInSlot(0).getItem() instanceof QueenItem ) {
            IBeeEffect effect = (IBeeEffect) GeneticHelper.getGeneValue(beeItems.getStackInSlot(0), GeneEffect.ID, true);
            if (effect != null)
                effect.runEffect(this, beeItems.getStackInSlot(0), cycleProgress);
        }
    }

    private void tryEmptyBuffer() {
        while (!outputBuffer.empty()) {
            ItemStack next = outputBuffer.pop();
            next = ItemHandlerHelper.insertItem(outputItems, next, false);
            if (next == ItemStack.EMPTY) {
                setChanged();
                removeError(EnumErrorCodes.OUTPUT_FULL);
            } else {
                outputBuffer.push(next);
                addError(EnumErrorCodes.OUTPUT_FULL);
                break;
            }
        }
    }

    private ItemStack createQueenFromPrincessAndDrone(ItemStack princess, ItemStack drone) {
        ItemStack queen = new ItemStack(ItemsRegistration.QUEEN.get());
        GeneticHelper.setGenome(queen, GeneticHelper.getGenome(princess));
        GeneticHelper.setMate(queen, GeneticHelper.getGenome(drone));
        QueenItem.setGeneration(queen, PrincessItem.getGeneration(princess));
        if (princess.getTag().contains(BeeItem.ANALYZED_TAG)) {
            if (princess.getTag().getBoolean(BeeItem.ANALYZED_TAG)) {
                queen.getOrCreateTag().putBoolean(BeeItem.ANALYZED_TAG, true);
            }
        }
        return queen;
    }

    //hook for effects to add to output
    public void addToOutput(ItemStack stack) {
        outputBuffer.add(stack);
    }

    @Override
    public boolean isQueenSatisfied() {
        return beeLogic.isQueenSatisfied();
    }

    @Override
    public boolean isQueenEcstatic() {
        return beeLogic.isQueenEcstatic();
    }

    @Override
    public void beeTick() {
        ItemStack top_stack = beeItems.getStackInSlot(0);
        ageQueen(top_stack);
        generateProduce(top_stack);
    }

    public void generateProduce(ItemStack bee) {
        Species species = (Species) GeneticHelper.getGeneValue(bee, GeneSpecies.ID, true);
        float housingModifiers = getHousingModifiers().stream().map(BeeHousingModifier::getProductivityMod).reduce(1f, (cur, next) -> cur * next);
        for (Product product : species.getProducts()) {
            outputBuffer.add(product.getStackResult(((EnumProductivity) GeneticHelper.getGeneValue(bee, GeneProductivity.ID, true)).value, housingModifiers));
        }
        if (errorState == EnumErrorCodes.ECSTATIC.value) {
            for (Product special : species.getSpecialtyProducts()) {
                outputBuffer.add(special.getStackResult(((EnumProductivity) GeneticHelper.getGeneValue(bee, GeneProductivity.ID, true)).value, housingModifiers));
            }
        }
        setChanged();
    }

    public void ageQueen(ItemStack queen) {
        float ageFactor = 1;
        for (BeeHousingModifier mod : getHousingModifiers()) {
            ageFactor /= mod.getLifespanMod();
        }
        BeeItem.setAge(queen, BeeItem.getAge(queen) + ageFactor);
        damageFrames();
        if (BeeItem.getAge(queen) >= ((EnumLifespan) GeneticHelper.getGeneValue(queen, GeneLifespan.ID, true)).value) {
            errorState = 0;
            float mutationMod = getHousingModifiers().stream().map(BeeHousingModifier::getMutationMod).reduce(1f, (a, b) -> a * b);
            outputBuffer.add(GeneticHelper.getOffspring(queen, ItemsRegistration.PRINCESS.get(), getLevel(), getBlockPos(), mutationMod));
            for (int i = 0; i < (int) GeneticHelper.getGeneValue(queen, GeneFertility.ID, true); i++) {
                outputBuffer.add(GeneticHelper.getOffspring(queen, ItemsRegistration.DRONE.get(), getLevel(), getBlockPos(), mutationMod));
            }
            beeItems.extractItem(BEE_SLOT, 1, false);
            setChanged();
        }
    }

    public List<BeeHousingModifier> getHousingModifiers() {
        List<BeeHousingModifier> modifiers = new ArrayList<>();
        for (int i = 0; i < frameItems.getSlots(); i++) {
            ItemStack item = frameItems.getStackInSlot(i);
            if (item.getItem() instanceof FrameItem frame)
                modifiers.add(frame.getModifier());
        }
        return modifiers;
    }

    public void damageFrames() {
        for (int i = 0; i < frameItems.getSlots(); i++) {
            if (frameItems.getStackInSlot(i).hurt(1, getLevel().random, null))
                frameItems.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private void increaseMatingProgress() {
        matingProgress++;
        setChanged();
    }

    private boolean hasFinished() {
        return matingProgress >= maxMatingProgress;
    }

    private void resetMatingProgress() {
        matingProgress = 0;
    }

    @Override
    public void addError(EnumErrorCodes... error) {
        for (EnumErrorCodes err : error) {
            errorState |= err.value;
        }
    }

    @Override
    public void removeError(EnumErrorCodes... error) {
        for (EnumErrorCodes err : error) {
            errorState = (errorState & (err.value ^ Integer.MAX_VALUE));
        }
    }

    @Override
    public int getErrors() {
        return data.get(2);
    }
}
