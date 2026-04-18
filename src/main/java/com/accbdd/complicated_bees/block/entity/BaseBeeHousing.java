package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.*;
import com.accbdd.complicated_bees.bees.effect.IBeeEffect;
import com.accbdd.complicated_bees.bees.gene.*;
import com.accbdd.complicated_bees.bees.gene.enums.EnumLifespan;
import com.accbdd.complicated_bees.bees.gene.enums.EnumProductivity;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.item.*;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseBeeHousing extends BlockEntity implements IBeeHousing {
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
    public static final String OUTPUT_BUFFER_TAG = "output_buffer";

    public static final int CYCLE_LENGTH = ServerConfig.SERVER_CONFIG.productionCycleLength.get();
    public static final String CYCLE_TAG = "cycle";
    public static final int SATISFY_CYCLE_LENGTH = ServerConfig.SERVER_CONFIG.enviroCycleLength.get();

    public static final int MAX_MULT = ServerConfig.SERVER_CONFIG.productivityCap.get();

    public static final String OWNER_TAG = "owner";
    private UUID owner = null;

    private final ContainerData data;
    private int cycleProgress = 0;
    private int satisfyCycleProgress = 0;
    private int matingProgress = 0;
    private int maxMatingProgress = 20;
    private int errorState = 0;

    private final BeeLogic beeLogic;

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        getItemHandler().invalidate();
        getBeeItemHandler().invalidate();
        getOutputItemHandler().invalidate();
        getFrameItemHandler().invalidate();
    }

    public BaseBeeHousing(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        this.beeLogic = new BeeLogic(getLevel(), getBlockPos(), this);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> matingProgress;
                    case 1 -> maxMatingProgress;
                    case 2 -> errorState;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> matingProgress = value;
                    case 1 -> maxMatingProgress = value;
                    case 2 -> errorState = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public abstract ItemStackHandler getBeeItems();

    public abstract ItemStackHandler getOutputItems();

    public abstract ItemStackHandler getFrameItems();

    public abstract LazyOptional<IItemHandlerModifiable> getItemHandler();

    public abstract LazyOptional<IItemHandlerModifiable> getBeeItemHandler();

    public abstract LazyOptional<IItemHandlerModifiable> getOutputItemHandler();

    public abstract LazyOptional<IItemHandlerModifiable> getFrameItemHandler();

    public abstract Stack<ItemStack> getOutputBuffer();

    public abstract BeeLogic getLogic();

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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(CYCLE_TAG, IntTag.valueOf(cycleProgress));
        tag.put(ITEMS_BEES_TAG, getBeeItems().serializeNBT(registries));
        tag.put(ITEMS_OUTPUT_TAG, getOutputItems().serializeNBT(registries));
        tag.put(FRAME_SLOT_TAG, getFrameItems().serializeNBT(registries));
        if (getOwner() != null)
            tag.putUUID(OWNER_TAG, getOwner());
        ListTag bufferTag = new ListTag();
        for (ItemStack stack : getOutputBuffer()) {
            bufferTag.add(stack.save(registries));
        }
        tag.put(OUTPUT_BUFFER_TAG, bufferTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        cycleProgress = tag.getInt(CYCLE_TAG);
        if (tag.contains(ITEMS_BEES_TAG)) {
            getBeeItems().deserializeNBT(registries, tag.getCompound(ITEMS_BEES_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            getOutputItems().deserializeNBT(registries, tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        if (tag.contains(FRAME_SLOT_TAG)) {
            getFrameItems().deserializeNBT(registries, tag.getCompound(FRAME_SLOT_TAG));
        }
        if (tag.contains(OUTPUT_BUFFER_TAG)) {
            for (Tag itemCompound : tag.getList(OUTPUT_BUFFER_TAG, Tag.TAG_COMPOUND)) {
                getOutputBuffer().add(ItemStack.parseOptional(registries, (CompoundTag) itemCompound));
            }
        }
        if (tag.contains(OWNER_TAG))
            setOwner(tag.getUUID(OWNER_TAG));
        satisfyCycleProgress = new Random().nextInt(0, SATISFY_CYCLE_LENGTH);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLogic() != null)
            getLogic().setLevel(getLevel());
    }

    public void tickServer() {
        ItemStack top_stack = getBeeItems().getStackInSlot(0);
        ItemStack bottom_stack = getBeeItems().getStackInSlot(1);

        //empty buffer
        if (!getOutputBuffer().empty()) {
            tryEmptyBuffer();
        }

        //mate
        if (top_stack.getItem() instanceof PrincessItem && bottom_stack.getItem() instanceof DroneItem) {
            increaseMatingProgress();
            if (hasFinished()) {
                resetMatingProgress();
                getBeeItems().extractItem(1, 1, false);
                getBeeItems().setStackInSlot(0, GeneticHelper.createQueenFromPrincessAndDrone(top_stack, bottom_stack));
                getLogic().setLevel(getLevel());
                getLogic().rebuildFlowerCache();
                getLogic().checkConditions();
            }
        } else {
            resetMatingProgress();
        }

        //check if queen is satisfied
        if (satisfyCycleProgress >= SATISFY_CYCLE_LENGTH) {
            if (top_stack.getItem() instanceof QueenItem) {
                getLogic().setQueen(top_stack);
                getLogic().checkConditions();
                satisfyCycleProgress = 0;
            }
        } else {
            satisfyCycleProgress++;
        }

        //do queen cycle
        if (top_stack.getItem() instanceof QueenItem) {
            if (getLogic().isQueenSatisfied()) {
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
        if (getBeeItems().getStackInSlot(0).getItem() instanceof QueenItem ) {
            IBeeEffect effect = (IBeeEffect) GeneticHelper.getGeneValue(getBeeItems().getStackInSlot(0), GeneEffect.ID, true);
            if (effect != null)
                effect.runEffect(this, getBeeItems().getStackInSlot(0), cycleProgress);
        }
    }

    private void tryEmptyBuffer() {
        while (!getOutputBuffer().empty()) {
            ItemStack next = getOutputBuffer().pop();
            next = ItemHandlerHelper.insertItem(getOutputItems(), next, false);
            if (next == ItemStack.EMPTY || next.is(Items.AIR)) {
                setChanged();
                removeError(EnumErrorCodes.OUTPUT_FULL);
            } else {
                getOutputBuffer().push(next);
                addError(EnumErrorCodes.OUTPUT_FULL);
                break;
            }
        }
    }

    @Override
    public void addToOutput(ItemStack stack) {
        getOutputBuffer().add(stack);
    }

    @Override
    public boolean isQueenSatisfied() {
        return getLogic().isQueenSatisfied();
    }

    @Override
    public boolean isQueenEcstatic() {
        return getLogic().isQueenEcstatic();
    }

    @Override
    public void beeTick() {
        ItemStack top_stack = getBeeItems().getStackInSlot(0);
        ageQueen(top_stack);
        generateProduce(top_stack);
        damageFrames();
    }

    public void generateProduce(ItemStack bee) {
        Species species = (Species) GeneticHelper.getGeneValue(bee, GeneSpecies.ID, true);
        float housingModifiers = getHousingModifiers().stream().map(BeeHousingModifier::getProductivityMod).reduce(1f, (cur, next) -> cur * next);
        housingModifiers = (MAX_MULT * housingModifiers) / (housingModifiers + MAX_MULT);
        for (Product product : species.getProducts()) {
            getOutputBuffer().add(product.getStackResult(((EnumProductivity) GeneticHelper.getGeneValue(bee, GeneProductivity.ID, true)).value, housingModifiers));
        }
        if (getErrors() == EnumErrorCodes.ECSTATIC.value) {
            for (Product special : species.getSpecialtyProducts()) {
                getOutputBuffer().add(special.getStackResult(((EnumProductivity) GeneticHelper.getGeneValue(bee, GeneProductivity.ID, true)).value, housingModifiers));
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
        if (BeeItem.getAge(queen) >= ((EnumLifespan) GeneticHelper.getGeneValue(queen, GeneLifespan.ID, true)).value) {
            produceOffspring(queen, getBlockPos());
        }
    }

    public void produceOffspring(ItemStack queen, BlockPos pos) {
        errorState = 0;
        float mutationMod = getHousingModifiers().stream().map(BeeHousingModifier::getMutationMod).reduce(1f, (a, b) -> a * b);
        getOutputBuffer().add(GeneticHelper.getOffspring(queen, ItemsRegistration.PRINCESS.get(), getLevel(), pos, mutationMod));
        for (int i = 0; i < (int) GeneticHelper.getGeneValue(queen, GeneFertility.ID, true); i++) {
            getOutputBuffer().add(GeneticHelper.getOffspring(queen, ItemsRegistration.DRONE.get(), getLevel(), pos, mutationMod));
        }
        getBeeItems().extractItem(BEE_SLOT, 1, false);
        setChanged();
    }

    public List<BeeHousingModifier> getHousingModifiers() {
        List<BeeHousingModifier> modifiers = new ArrayList<>();
        for (int i = 0; i < getFrameItems().getSlots(); i++) {
            ItemStack item = getFrameItems().getStackInSlot(i);
            if (item.getItem() instanceof FrameItem frame)
                modifiers.add(frame.getModifier());
        }
        return modifiers;
    }

    public void damageFrames() {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < getFrameItems().getSlots(); i++) {
                getFrameItems().getStackInSlot(i).hurtAndBreak(1, serverLevel, null, item -> {});
            }
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
