package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class MutatorRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Item input;
    private final float mutationChance;

    public static final RecipeSerializer<MutatorRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public MutatorRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            ResourceLocation inputItemLocation = ResourceLocation.tryParse(json.get("item").getAsString());
            Item item = ForgeRegistries.ITEMS.getValue(inputItemLocation);
            if (item == null) throw new JsonParseException("could not parse input for " + pRecipeId);
            float mutationChance = json.get("modifier").getAsFloat();
            return new MutatorRecipe(pRecipeId, item, mutationChance);
        }

        @Override
        public @Nullable MutatorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new MutatorRecipe(pRecipeId, pBuffer.readItem().getItem(), pBuffer.readFloat());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MutatorRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.input.getDefaultInstance());
            pBuffer.writeFloat(pRecipe.mutationChance);
        }
    };

    public MutatorRecipe(ResourceLocation id, Item input, float mutationChance) {
        this.id = id;
        this.input = input;
        this.mutationChance = mutationChance;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return pContainer.getItem(0).is(input);
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

    public float getMutationChance() {
        return mutationChance;
    }
}
