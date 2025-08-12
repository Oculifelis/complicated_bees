package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.recipe.TempUnitRecipe;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MellariumTempUnitBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final String ITEMS_TAG = "Items";
    private final ItemStackHandler items;
    private final LazyOptional<IItemHandler> itemHandler;
    private final RecipeManager.CachedCheck<Container, TempUnitRecipe> quickCheck;

    public MellariumTempUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_TEMP_UNIT_BLOCK_ENTITY.get(), pPos, pBlockState);
        items = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return hasRecipe(stack);
            }
        };
        itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items));
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.TEMP_UNIT_RECIPE.get());
    }

    private boolean hasRecipe(ItemStack stack) {
        Optional<TempUnitRecipe> recipeCheck = quickCheck.getRecipeFor(new SimpleContainer(stack), getLevel());
        return recipeCheck.isPresent();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put(ITEMS_TAG, items.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains(ITEMS_TAG))
            items.deserializeNBT(pTag.getCompound(ITEMS_TAG));
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
    public void invalidateCaps() {
        super.invalidateCaps();
        getItemHandler().invalidate();
    }

    @Override
    public BeeHousingModifier getModifier() {
        ItemStack stack = items.getStackInSlot(0);
        if (hasRecipe(stack)) {
            return new BeeHousingModifier.Builder().temperature(quickCheck.getRecipeFor(new SimpleContainer(stack), getLevel()).get().getTempChange()).build();
        }
        return new BeeHousingModifier();
    }

    @Override
    public void onBeeTick() {
        ItemStack stack = items.getStackInSlot(0);
        if (hasRecipe(stack)) {
            if (level.getRandom().nextFloat() < quickCheck.getRecipeFor(new SimpleContainer(stack), getLevel()).get().getUseChance()) {
                if (stack.hasCraftingRemainingItem()) {
                    items.setStackInSlot(0, stack.getCraftingRemainingItem());
                    getLogic().getController().getLogic().clearConditionCache();
                    getLogic().getController().getLogic().checkConditions();
                } else {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        getLogic().getController().getLogic().clearConditionCache();
                        getLogic().getController().getLogic().checkConditions();
                    }
                }
            }
        }
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }
}
