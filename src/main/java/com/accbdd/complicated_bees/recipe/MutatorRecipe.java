package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record MutatorRecipe(Ingredient input, float mutationModifier) implements Recipe<RecipeInput> {
    public static class Serializer implements RecipeSerializer<MutatorRecipe> {
        public static final MapCodec<MutatorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(MutatorRecipe::input),
                        Codec.FLOAT.fieldOf("modifier").forGetter(MutatorRecipe::mutationModifier)
                ).apply(instance, MutatorRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, MutatorRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<MutatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MutatorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static MutatorRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            return new MutatorRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer), pBuffer.readFloat());
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, MutatorRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
            pBuffer.writeFloat(pRecipe.mutationModifier);
        }
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
        return EsotericRegistration.MUTATOR_RECIPE_SERIALIZER.get();
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
