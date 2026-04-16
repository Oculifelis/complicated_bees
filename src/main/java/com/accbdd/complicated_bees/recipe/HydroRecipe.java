package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HydroRecipe implements Recipe<RecipeInput> {
    private final Ingredient input;
    private final Product output;
    private final EnumTolerance humidityChange;
    private final float useChance;

    public static final RecipeSerializer<HydroRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public HydroRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"), false);
            Product output =  Product.CODEC.decode(JsonOps.INSTANCE, json.getAsJsonObject("output")).result().get().getFirst();
            EnumTolerance humidityChange = EnumTolerance.getFromString(json.get("humidity_change").getAsString());
            float useChance = json.get("use_chance").getAsFloat();
            return new HydroRecipe(pRecipeId, input, output, humidityChange, useChance);
        }

        @Override
        public @Nullable HydroRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new HydroRecipe(pRecipeId, Ingredient.fromNetwork(pBuffer), Product.fromNetwork(pBuffer), pBuffer.readEnum(EnumTolerance.class), pBuffer.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, HydroRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pRecipe.getOutput().toNetwork(pBuffer);
            pBuffer.writeEnum(pRecipe.humidityChange);
            pBuffer.writeFloat(pRecipe.useChance);
        }
    };

    public HydroRecipe(Ingredient input, Product output, EnumTolerance humidityChange, float useChance) {
        this.input = input;
        this.output = output;
        this.humidityChange = humidityChange;
        this.useChance = useChance;
    }

    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.HYDROREGULATOR_RECIPE.get();
    }

    public EnumTolerance getHumidityChange() {
        return humidityChange;
    }

    public float getUseChance() {
        return useChance;
    }

    public Ingredient getInput() {
        return input;
    }

    public Product getOutput() {
        return output;
    }
}
