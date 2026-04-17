package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.recipe.MutatorRecipe;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MellariumMutatorBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final String ITEMS_TAG = "Items";
    private final ItemStackHandler items;
    private final LazyOptional<IItemHandler> itemHandler;
    private final RecipeManager.CachedCheck<RecipeInput, MutatorRecipe> quickCheck;

    public MellariumMutatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_MUTATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.MUTATOR_RECIPE.get());
        items = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return hasRecipe(stack);
            }
        };
        itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items));
    }

    private boolean hasRecipe(ItemStack stack) {
        Optional<MutatorRecipe> recipeCheck = quickCheck.getRecipeFor(new SimpleContainer(stack), getLevel());
        return recipeCheck.isPresent();
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
        super.saveAdditional(pTag);
        pTag.put(ITEMS_TAG, items.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.load(pTag);
        if (pTag.contains(ITEMS_TAG))
            items.deserializeNBT(registries, pTag.getCompound(ITEMS_TAG));
    }

    @Override
    public BeeHousingModifier getModifier() {
        ItemStack stack = items.getStackInSlot(0);
        if (hasRecipe(stack)) {
            return new BeeHousingModifier.Builder().mutation(quickCheck.getRecipeFor(new RecipeWrapper(new InvWrapper(new SimpleContainer(stack))), getLevel()).get().value().getMutationModifier()).build();
        }
        return new BeeHousingModifier();
    }

    @Override
    public void onDeath() {
        items.getStackInSlot(0).shrink(1);
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }
}
