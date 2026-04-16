package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TempUnitRecipe implements Recipe<RecipeInput> {
    private final Ingredient input;
    private final EnumTolerance tempChange;
    private final float useChance;

    public static final RecipeSerializer<TempUnitRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public TempUnitRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"), false);
            EnumTolerance tempChange = EnumTolerance.getFromString(json.get("temp_change").getAsString());
            float useChance = json.get("use_chance").getAsFloat();
            return new TempUnitRecipe(pRecipeId, input, tempChange, useChance);
        }

        @Override
        public @Nullable TempUnitRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new TempUnitRecipe(pRecipeId, Ingredient.fromNetwork(pBuffer), pBuffer.readEnum(EnumTolerance.class), pBuffer.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, TempUnitRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeEnum(pRecipe.tempChange);
            pBuffer.writeFloat(pRecipe.useChance);
        }
    };

    public TempUnitRecipe(ResourceLocation id, Ingredient input, EnumTolerance tempChange, float useChance) {
        this.input = input;
        this.tempChange = tempChange;
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
        return EsotericRegistration.TEMP_UNIT_RECIPE.get();
    }

    public EnumTolerance getTempChange() {
        return tempChange;
    }

    public float getUseChance() {
        return useChance;
    }

    public Ingredient getInput() {
        return input;
    }
}
