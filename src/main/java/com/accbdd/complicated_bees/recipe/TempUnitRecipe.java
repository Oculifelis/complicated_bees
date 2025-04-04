package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TempUnitRecipe implements Recipe<Container> {
    private final ResourceLocation id;
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
        this.id = id;
        this.input = input;
        this.tempChange = tempChange;
        this.useChance = useChance;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
