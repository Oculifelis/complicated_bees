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

public class MutatorRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final float mutationModifier;

    public static final RecipeSerializer<MutatorRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public MutatorRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"), false);
            float mutationChance = json.get("modifier").getAsFloat();
            return new MutatorRecipe(pRecipeId, input, mutationChance);
        }

        @Override
        public MutatorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new MutatorRecipe(pRecipeId, Ingredient.fromNetwork(pBuffer), pBuffer.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MutatorRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeFloat(pRecipe.mutationModifier);
        }
    };

    public MutatorRecipe(ResourceLocation id, Ingredient input, float mutationModifier) {
        this.id = id;
        this.input = input;
        this.mutationModifier = mutationModifier;
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
        return EsotericRegistration.MUTATOR_RECIPE.get();
    }

    public float getMutationModifier() {
        return mutationModifier;
    }

    public Ingredient getInput() {
        return input;
    }
}
