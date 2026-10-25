package com.accbdd.complicated_bees.port;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

/** Keeps machine recipe outputs distinct from vanilla's single-output crafting UI. */
public interface MachineRecipe<T extends RecipeInput> extends Recipe<T> {
    ItemStack assemble(T input, HolderLookup.Provider registries);
    ItemStack getResultItem(HolderLookup.Provider registries);
    boolean canCraftInDimensions(int width, int height);
    @Override default ItemStack assemble(T input) { return assemble(input, null); }
    @Override default boolean showNotification() { return false; }
    @Override default boolean isSpecial() { return true; }
    @Override default String group() { return ""; }
    @Override default PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }
    @Override default RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
}
