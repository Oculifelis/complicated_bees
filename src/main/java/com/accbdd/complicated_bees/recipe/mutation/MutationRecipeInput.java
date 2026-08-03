package com.accbdd.complicated_bees.recipe.mutation;

import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MutationRecipeInput(ResourceLocation first, ResourceLocation second) implements RecipeInput {
    public MutationRecipeInput(Species first, Species second) {
        this(SpeciesRegistration.getResourceLocation(first), SpeciesRegistration.getResourceLocation(second));
    }

    @Override
    public ItemStack getItem(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 2;
    }
}
