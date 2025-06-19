package com.accbdd.complicated_bees.recipe;

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

public class HoneyGeneratorRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final int burnTime;

    public static final RecipeSerializer<HoneyGeneratorRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public HoneyGeneratorRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.getAsJsonObject("input"), false);
            int burnTime = json.get("burn_time").getAsInt();
            return new HoneyGeneratorRecipe(pRecipeId, input, burnTime);
        }

        @Override
        public @Nullable HoneyGeneratorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new HoneyGeneratorRecipe(pRecipeId, Ingredient.fromNetwork(pBuffer), pBuffer.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, HoneyGeneratorRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeInt(pRecipe.getBurnTime());
        }
    };

    public HoneyGeneratorRecipe(ResourceLocation id, Ingredient input, int burnTime) {
        this.id = id;
        this.input = input;
        this.burnTime = burnTime;
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
        return EsotericRegistration.HONEY_GENERATOR_RECIPE.get();
    }

    public Ingredient getInput() {
        return input;
    }

    public int getBurnTime() {
        return burnTime;
    }
}
