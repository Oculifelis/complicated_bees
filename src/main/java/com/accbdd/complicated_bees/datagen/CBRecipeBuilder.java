package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class CBRecipeBuilder {
    public static class MutatorRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item input;
        private final float modifier;

        public MutatorRecipe(ResourceLocation id, Item input, float modifier) {
            this.id = id;
            this.input = input;
            this.modifier = modifier;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.addProperty("item", ForgeRegistries.ITEMS.getKey(input).toString());
            pJson.addProperty("modifier", modifier);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return EsotericRegistration.MUTATOR_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    public static class TempUnitRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item input;
        private final EnumTolerance tempChange;
        private final float useChance;

        public TempUnitRecipe(ResourceLocation id, Item input, EnumTolerance tempChange, float useChance) {
            this.id = id;
            this.input = input;
            this.tempChange = tempChange;
            this.useChance = useChance;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.addProperty("item", ForgeRegistries.ITEMS.getKey(input).toString());
            pJson.addProperty("temp_change", tempChange.toString());
            pJson.addProperty("use_chance", useChance);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return EsotericRegistration.TEMP_UNIT_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
