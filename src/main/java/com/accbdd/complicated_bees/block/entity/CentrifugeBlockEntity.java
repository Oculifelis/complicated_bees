package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.item.UpgradeItem;
import com.accbdd.complicated_bees.recipe.CentrifugeRecipe;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class CentrifugeBlockEntity extends BlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int INPUT_SLOT_COUNT = 1;
    public static final String ITEMS_INPUT_TAG = "input_items";

    public static final int OUTPUT_SLOT = 0;
    public static final int OUTPUT_SLOT_COUNT = 9;
    public static final String ITEMS_OUTPUT_TAG = "output_items";

    public static final int UPGRADE_SLOT = 0;
    public static final int UPGRADE_SLOT_COUNT = 3;
    public static final String ITEMS_UPGRADE_TAG = "upgrade_items";

    public static final int SLOT_COUNT = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT + UPGRADE_SLOT_COUNT;

    public final Stack<ItemStack> outputBuffer = new Stack<>();
    public static final String OUTPUT_BUFFER_TAG = "output_buffer";

    public static final String ENERGY_TAG = "energy";
    public static final int CAPACITY = 100000;
    public static final int MAXTRANSFER = 5000;
    public static final int BASE_USAGE = ServerConfig.SERVER_CONFIG.centrifugeBaseEnergy.get();
    public static final int BASE_MAX_PROGRESS = ServerConfig.SERVER_CONFIG.centrifugeBaseSpeed.get();

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = BASE_MAX_PROGRESS;
    private int usage = BASE_USAGE;
    private final RecipeManager.CachedCheck<Container, CentrifugeRecipe> quickCheck;

    private final ItemStackHandler inputItems = createInputHandler(INPUT_SLOT_COUNT);
    private final ItemStackHandler outputItems = createItemHandler(OUTPUT_SLOT_COUNT);
    private final ItemStackHandler upgradeItems = createUpgradeHandler(UPGRADE_SLOT_COUNT);
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems) {
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    });
    public final LazyOptional<IItemHandler> upgradeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(upgradeItems) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof UpgradeItem;
        }
    });
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new CombinedInvWrapper((IItemHandlerModifiable) outputItemHandler.resolve().get(), (IItemHandlerModifiable) inputItemHandler.resolve().get()));

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
        energyHandler.invalidate();
        upgradeItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.getItemHandler().cast();
        }
        if (cap == ForgeCapabilities.ENERGY)
            return this.getEnergyHandler().cast();
        return super.getCapability(cap, side);
    }

    public CentrifugeBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesRegistration.CENTRIFUGE_ENTITY.get(), pos, blockState);
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.CENTRIFUGE_RECIPE.get());
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CentrifugeBlockEntity.this.progress;
                    case 1 -> CentrifugeBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CentrifugeBlockEntity.this.progress = value;
                    case 1 -> CentrifugeBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public ItemStackHandler getUpgradeItems() {
        return upgradeItems;
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<IItemHandler> getInputItemHandler() {
        return inputItemHandler;
    }

    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    public LazyOptional<IItemHandler> getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    private final EnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            setChanged();
            return super.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    });

    private ItemStackHandler createItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    private ItemStackHandler createInputHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return getRecipe(stack) != null;
            }
        };
    }

    private ItemStackHandler createUpgradeHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                calculateUpgradeStats();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }

    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return energyHandler;
    }

    public int getStoredPower() {
        return energy.getEnergyStored();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ITEMS_INPUT_TAG, inputItems.serializeNBT());
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT());
        tag.put(ITEMS_UPGRADE_TAG, upgradeItems.serializeNBT());
        tag.put(ENERGY_TAG, energy.serializeNBT());
        ListTag bufferTag = new ListTag();
        for (ItemStack stack : outputBuffer) {
            bufferTag.add(stack.save(new CompoundTag()));
        }
        tag.put(OUTPUT_BUFFER_TAG, bufferTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ITEMS_INPUT_TAG)) {
            inputItems.deserializeNBT(tag.getCompound(ITEMS_INPUT_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        if (tag.contains(OUTPUT_BUFFER_TAG)) {
            for (Tag itemCompound : tag.getList(OUTPUT_BUFFER_TAG, Tag.TAG_COMPOUND)) {
                outputBuffer.add(ItemStack.of((CompoundTag) itemCompound));
            }
        }
        if (tag.contains(ITEMS_UPGRADE_TAG)) {
            upgradeItems.deserializeNBT(tag.getCompound(ITEMS_UPGRADE_TAG));
        }
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(tag.get(ENERGY_TAG));
        }
    }

    public void tickServer() {
        if (!outputBuffer.empty()) {
            tryEmptyBuffer();
        }

        ItemStack stack = this.inputItems.getStackInSlot(INPUT_SLOT);
        if (getRecipe(stack) != null && canInsertIntoOutput(stack) && energy.getEnergyStored() > usage && outputBuffer.empty()) {
            if (!getBlockState().getValue(BlockStateProperties.POWERED)) {
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true));
            }
            increaseCraftingProgress();
            setChanged();
            if (hasFinished()) {
                craftItem(stack);
                resetProgress();
            }
        } else {
            if (getBlockState().getValue(BlockStateProperties.POWERED)) {
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false));
            }
            lowerProgress();
        }
    }

    private void lowerProgress() {
        if (progress > 0) {
            progress--;
        }
    }

    private void tryEmptyBuffer() {
        while (!outputBuffer.empty()) {
            ItemStack next = outputBuffer.pop();
            next = ItemHandlerHelper.insertItem(outputItems, next, false);
            if (next == ItemStack.EMPTY) {
                setChanged();
            } else {
                outputBuffer.push(next);
                break;
            }
        }
    }

    private void increaseCraftingProgress() {
        energy.extractEnergy(usage, false);
        progress++;
    }

    private void resetProgress() {
        progress = 0;
    }

    @Nullable
    private CentrifugeRecipe getRecipe(ItemStack stack) {
        Optional<CentrifugeRecipe> recipeCheck = quickCheck.getRecipeFor(getWrapper(stack), getLevel());
        return recipeCheck.orElse(null);
    }

    private boolean hasFinished() {
        return progress >= maxProgress;
    }

    private void craftItem(ItemStack stack) {
        CentrifugeRecipe recipe = getRecipe(stack);
        if (recipe == null)
            return;
        List<Product> products = getRecipe(stack).getOutputs();
        this.inputItems.extractItem(INPUT_SLOT, 1, false);

        for (Product product : products) {
            outputBuffer.push(product.getStackResult());
        }
    }

    /**
     * @param stack the stack to test
     * @return whether the CentrifugeRecipe given by the stack's primary output can be output to the centrifuge's output slots.
     */
    private boolean canInsertIntoOutput(ItemStack stack) {
        CentrifugeRecipe recipe = getRecipe(stack);
        if (recipe == null)
            return true;
        ItemStack primary = ItemStack.EMPTY;
        if (!recipe.getOutputs().isEmpty()) {
            primary = recipe.getOutputs().get(0).getStack();
        }
        boolean canInsert = false;
        int stackCount = primary.getCount();
        for (int i = 0; i < OUTPUT_SLOT_COUNT; i++) {
            primary = this.outputItems.insertItem(i, primary, true);
            canInsert = canInsert || (primary.getCount() < stackCount);
        }
        return canInsert;
    }

    public ContainerData getData() {
        return data;
    }

    public RecipeWrapper getWrapper(ItemStack stack) {
        return new RecipeWrapper(new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, stack)));
    }

    public float getEfficiencyMod() {
        float mod = 1f;
        for (int i = 0; i < upgradeItems.getSlots(); i++) {
            if (upgradeItems.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                mod *= upgrade.getEfficiencyMod();
            }
        }
        return 1 / mod; //efficiency means usage is less, not more
    }

    public float getSpeedMod() {
        float mod = 1f;
        for (int i = 0; i < upgradeItems.getSlots(); i++) {
            if (upgradeItems.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                mod *= upgrade.getSpeedMod();
            }
        }
        return 1 / mod; //max progress is less, not more
    }

    public void calculateUpgradeStats() {
        maxProgress = Math.round(BASE_MAX_PROGRESS * getSpeedMod());
        usage = Math.round(BASE_USAGE * getEfficiencyMod());
    }
}
