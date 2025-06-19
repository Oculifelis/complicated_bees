package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.recipe.HoneyGeneratorRecipe;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class HoneyGeneratorBlockEntity extends BaseGeneratorBlockEntity {
    public static final int BASE_GENERATE = ServerConfig.SERVER_CONFIG.honeyGeneratorBaseEnergy.get();
    public static final int BASE_TRANSFER = ServerConfig.SERVER_CONFIG.honeyGeneratorBaseTransfer.get();
    public static final int BASE_STORAGE = ServerConfig.SERVER_CONFIG.honeyGeneratorBaseStorage.get();
    private final RecipeManager.CachedCheck<Container, HoneyGeneratorRecipe> quickCheck;

    public HoneyGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistration.HONEY_GENERATOR_BLOCK_ENTITY.get(), pos, state, BASE_GENERATE, BASE_TRANSFER, BASE_STORAGE);
        this.quickCheck = RecipeManager.createCheck(EsotericRegistration.HONEY_GENERATOR_RECIPE.get());
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return quickCheck.getRecipeFor(getWrapper(stack), getLevel()).isPresent();
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        if (isValidInput(stack))
            return quickCheck.getRecipeFor(getWrapper(stack), getLevel()).get().getBurnTime();
        return 0;
    }

    @NotNull
    private static RecipeWrapper getWrapper(ItemStack stack) {
        return new RecipeWrapper(new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, stack)));
    }
}
