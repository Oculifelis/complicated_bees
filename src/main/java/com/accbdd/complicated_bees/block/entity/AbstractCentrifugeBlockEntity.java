package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.recipe.CentrifugeRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

public abstract class AbstractCentrifugeBlockEntity extends BlockEntity implements ICentrifuge {
    public static final String ITEMS_INPUT_TAG = "input_items";
    public static final String ITEMS_OUTPUT_TAG = "output_items";
    public static final String ITEMS_UPGRADE_TAG = "upgrade_items";
    public static final String ENERGY_TAG = "energy";

    protected ItemStackHandler inputItems = createInputHandler();
    protected ItemStackHandler outputItems = createOutputHandler();
    protected ItemStackHandler upgradeItems = createUpgradeHandler();
    protected IEnergyStorage energyStorage = createEnergyStorage();

    public final Stack<ItemStack> outputBuffer = new Stack<>();
    public static final String OUTPUT_BUFFER_TAG = "output_buffer";

    private final ContainerData data;

    private final RecipeManager.CachedCheck<RecipeInput, CentrifugeRecipe> quickCheck;

    private int progress;
    private int maxProgress;
    private int activeUsage;
    private int idleUsage;
    private int energyUsage;
    private boolean isCrafting;

    public AbstractCentrifugeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.CENTRIFUGE_RECIPE.get());
        this.activeUsage = getActiveEnergyUsage();
        this.idleUsage = getIdleEnergyUsage();
        this.maxProgress = getMaxProgress();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractCentrifugeBlockEntity.this.progress;
                    case 1 -> getMaxProgress();
                    case 2 -> getEnergyUsage();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbstractCentrifugeBlockEntity.this.progress = value;
                    case 1 -> setMaxProgress(value);
                    case 2 -> setEnergyUsage(value);
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        getItemHandler().invalidate();
        getInputItemHandler().invalidate();
        getOutputItemHandler().invalidate();
        getUpgradeItemHandler().invalidate();
        getEnergyHandler().invalidate();
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_INPUT_TAG, inputItems.serializeNBT(registries));
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT(registries));
        tag.put(ITEMS_UPGRADE_TAG, upgradeItems.serializeNBT(registries));
        ListTag bufferTag = new ListTag();
        for (ItemStack stack : outputBuffer) {
            bufferTag.add(stack.save(registries));
        }
        tag.put(OUTPUT_BUFFER_TAG, bufferTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEMS_INPUT_TAG)) {
            inputItems.deserializeNBT(registries, tag.getCompound(ITEMS_INPUT_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(registries, tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        if (tag.contains(OUTPUT_BUFFER_TAG)) {
            for (Tag itemCompound : tag.getList(OUTPUT_BUFFER_TAG, Tag.TAG_COMPOUND)) {
                outputBuffer.add(ItemStack.parseOptional(registries, (CompoundTag) itemCompound));
            }
        }
        if (tag.contains(ITEMS_UPGRADE_TAG)) {
            upgradeItems.deserializeNBT(registries, tag.getCompound(ITEMS_UPGRADE_TAG));
        }
    }

    public void tickServer() {
        if (!outputBuffer.empty()) {
            tryEmptyBuffer();
        }

        if (energyStorage == null)
            return;

        energyStorage.extractEnergy(getIdleEnergyUsage(), false);

        if (!getCurrentlyProcessing().isEmpty() && outputBuffer.empty()) {
            if (energyStorage.getEnergyStored() > getActiveEnergyUsage()) {
                isCrafting = true;
                energyStorage.extractEnergy(getActiveEnergyUsage(), false);
                progress++;
                setChanged();
                if (progress >= getMaxProgress()) {
                    for (ItemStack stack : getCurrentlyProcessing()) {
                        craftItem(stack);
                    }
                    setProgress(0);
                }
            } else {
                lowerProgress();
            }
        } else { //no stacks being processed or output buffer has stuff in it
            lowerProgress();
        }
    }

    private void lowerProgress() {
        isCrafting = false;
        if (progress > 0) {
            progress--;
        }
    }

    private void tryEmptyBuffer() {
        while (!outputBuffer.empty()) {
            ItemStack next = outputBuffer.pop();
            next = ItemHandlerHelper.insertItem(outputItems, next, false);
            if (next == ItemStack.EMPTY || next.is(Items.AIR)) {
                setChanged();
            } else {
                outputBuffer.push(next);
                break;
            }
        }
    }

    @Nullable
    public CentrifugeRecipe getRecipe(ItemStack stack) {
        Optional<RecipeHolder<CentrifugeRecipe>> recipeCheck = quickCheck.getRecipeFor(getWrapper(stack), getLevel());
        return recipeCheck.isPresent() ? recipeCheck.get().value() : null;
    }

    /**
     * @param stack shrinks the given stack and adds its recipe output to the output buffer
     */
    private void craftItem(ItemStack stack) {
        CentrifugeRecipe recipe = getRecipe(stack);
        if (recipe == null)
            return;
        List<Product> products = getRecipe(stack).outputs();
        stack.shrink(1);

        for (Product product : products) {
            outputBuffer.push(product.getStackResult(getOutputMod()));
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
        if (!recipe.outputs().isEmpty()) {
            primary = recipe.outputs().getFirst().getStack();
        }
        boolean canInsert = false;
        int stackCount = primary.getCount();
        for (int i = 0; i < outputItems.getSlots(); i++) {
            primary = this.outputItems.insertItem(i, primary, true);
            canInsert = canInsert || (primary.getCount() < stackCount);
        }
        return canInsert;
    }

    public RecipeWrapper getWrapper(ItemStack stack) {
        return new RecipeWrapper(new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, stack)));
    }

    /**
     * @return the internal handler for input items
     */
    protected abstract ItemStackHandler createInputHandler();

    /**
     * @return the internal handler for output items
     */
    protected abstract ItemStackHandler createOutputHandler();

    /**
     * @return the internal handler for upgrade items
     */
    protected abstract ItemStackHandler createUpgradeHandler();

    /**
     * @return the internal handler for energy storage
     */
    protected abstract EnergyStorage createEnergyStorage();

    /**
     * @return a list of stacks this centrifuge is currently processing. should be empty if there are no recipes found
     */
    public abstract List<ItemStack> getCurrentlyProcessing();

    /**
     * @return a modifier to product chances
     */
    public abstract float getOutputMod();

    @Override
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int getActiveEnergyUsage() {
        return activeUsage;
    }

    @Override
    public int getIdleEnergyUsage() {
        return idleUsage;
    }

    @Override
    public int getEnergyUsage() {
        return energyUsage;
    }

    @Override
    public boolean isCrafting() {
        return isCrafting;
    }

    @Override
    public void setActiveEnergyUsage(int value) {
        this.activeUsage = value;
    }

    @Override
    public void setIdleEnergyUsage(int value) {
        this.idleUsage = value;
    }

    @Override
    public void setEnergyUsage(int value) {
        this.energyUsage = value;
    }

    public ContainerData getData() {
        return data;
    }
}
