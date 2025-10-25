package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FlowerIngredientHelper implements IIngredientHelper<Flower> {
    @Override
    public IIngredientType<Flower> getIngredientType() {
        return ComplicatedIngredients.FLOWER;
    }

    @Override
    public String getDisplayName(Flower flower) {
        return GeneticHelper.getTranslationKey(flower).getString();
    }

    @Override
    public String getUniqueId(Flower ingredient, UidContext context) {
        return getResourceLocation(ingredient).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(Flower ingredient) {
        return GeneticHelper.getRegistryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get().getKey(ingredient);
    }

    @Override
    public Flower copyIngredient(Flower ingredient) {
        return ingredient; //flower is immutable anyway... hopefully?
    }

    @Override
    public String getErrorInfo(@Nullable Flower ingredient) {
        return ingredient != null ? getResourceLocation(ingredient)+ ": " + ingredient.getBlocksAsResourceLocs().toString() : "flower is null!";
    }
}
