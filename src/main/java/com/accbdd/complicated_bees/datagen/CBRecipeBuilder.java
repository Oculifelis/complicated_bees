package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class CBRecipeBuilder {
    public static class MutatorRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input;
        private final float modifier;

        public MutatorRecipe(ResourceLocation id, Ingredient input, float modifier) {
            this.id = id;
            this.input = input;
            this.modifier = modifier;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("input", input.toJson());
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
        private final Ingredient input;
        private final EnumTolerance tempChange;
        private final float useChance;

        public TempUnitRecipe(ResourceLocation id, Ingredient input, EnumTolerance tempChange, float useChance) {
            this.id = id;
            this.input = input;
            this.tempChange = tempChange;
            this.useChance = useChance;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("input", input.toJson());
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

    public static class HydroRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input;
        private final Product output;
        private final EnumTolerance humidityChange;
        private final float useChance;

        public HydroRecipe(ResourceLocation id, Ingredient input, Product output, EnumTolerance humidityChange, float useChance) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.humidityChange = humidityChange;
            this.useChance = useChance;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("input", input.toJson());
            pJson.add("output", Product.CODEC.encodeStart(JsonOps.INSTANCE ,output).result().get());
            pJson.addProperty("humidity_change", humidityChange.toString());
            pJson.addProperty("use_chance", useChance);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return EsotericRegistration.HYDROREGULATOR_RECIPE_SERIALIZER.get();
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
