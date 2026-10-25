package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.Identifier;
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

    public String getUniqueId(Flower ingredient, UidContext context) {
        return getUid(ingredient, context);
    }

    @Override
    public String getUid(Flower ingredient, UidContext context) {
        return getIdentifier(ingredient).toString();
    }

    @Override
    public Identifier getIdentifier(Flower ingredient) {
        return GeneticHelper.getRegistryAccess().lookupOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).getKey(ingredient);
    }

    @Override
    public Flower copyIngredient(Flower ingredient) {
        return ingredient; //flower is immutable anyway... hopefully?
    }

    @Override
    public String getErrorInfo(@Nullable Flower ingredient) {
        return ingredient != null ? getIdentifier(ingredient)+ ": " + ingredient.getBlocksAsResourceLocs().toString() : "flower is null!";
    }
}
