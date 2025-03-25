package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

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

        protected MutatorRecipe(Item input, float modifier) {
            this(new ResourceLocation(MODID, "mutator/" + ForgeRegistries.ITEMS.getKey(input).getPath()), input, modifier);
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
}
