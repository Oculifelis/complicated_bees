package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.recipe.HydroRecipe;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class MellariumHydroregulatorBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final String ITEMS_TAG = "Items";
    private final ItemStackHandler inputItems;
    private final ItemStackHandler outputItems;
    private final LazyOptional<IItemHandlerModifiable> inputItemHandler;
    private final LazyOptional<IItemHandlerModifiable> outputItemHandler;
    private final LazyOptional<IItemHandlerModifiable> itemHandler;
    private final RecipeManager.CachedCheck<RecipeInput, HydroRecipe> quickCheck;

    public MellariumHydroregulatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_HYDROREGULATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.HYDROREGULATOR_RECIPE.get());
        inputItems = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return hasRecipe(stack);
            }
        };

        outputItems = new ItemStackHandler(1);
        inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        });
        outputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems) {
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }
        });

        itemHandler = LazyOptional.of(() -> new CombinedInvWrapper(inputItemHandler.resolve().get(), outputItemHandler.resolve().get()));
    }

    private boolean hasRecipe(ItemStack stack) {
        AtomicBoolean test = new AtomicBoolean(false);
        quickCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getLevel()).ifPresent(recipe ->
                test.set(canRecipeOutput(recipe.value()))
        );
        return test.get();
    }

    private boolean canRecipeOutput(HydroRecipe recipe) {
        return outputItems.insertItem(0, recipe.output().getStack(), true).isEmpty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.getItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        getItemHandler().invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        pTag.put(ITEMS_TAG, inputItems.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.contains(ITEMS_TAG))
            inputItems.deserializeNBT(registries, pTag.getCompound(ITEMS_TAG));
    }

    @Override
    public BeeHousingModifier getModifier() {
        ItemStack stack = inputItems.getStackInSlot(0);
        if (hasRecipe(stack)) {
            return new BeeHousingModifier.Builder().humidity(quickCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getLevel()).get().value().humidityChange()).build();
        }
        return new BeeHousingModifier();
    }

    @Override
    public void onBeeTick() {
        ItemStack stack = inputItems.getStackInSlot(0);
        quickCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getLevel()).ifPresent(recipe -> {
            getLogic().getController().ifPresent(controller -> {
                if (level.getRandom().nextFloat() < recipe.value().useChance()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        controller.getLogic().clearConditionCache();
                        controller.getLogic().checkConditions();
                    }
                    outputItems.insertItem(0, recipe.value().output().getStackResult(), false);
                }
            });
        });
    }

    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public LazyOptional<IItemHandlerModifiable> getItemHandler() {
        return itemHandler;
    }
}
