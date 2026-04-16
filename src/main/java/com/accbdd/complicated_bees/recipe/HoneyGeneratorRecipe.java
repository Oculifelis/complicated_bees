package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record HoneyGeneratorRecipe(Ingredient input, int burnTime) implements Recipe<RecipeInput> {
    public static class Serializer implements RecipeSerializer<HoneyGeneratorRecipe> {
        private static final MapCodec<HoneyGeneratorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> 
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(HoneyGeneratorRecipe::input),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("burn_time").forGetter(HoneyGeneratorRecipe::burnTime)
                ).apply(instance, HoneyGeneratorRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, HoneyGeneratorRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<HoneyGeneratorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HoneyGeneratorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static HoneyGeneratorRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            return new HoneyGeneratorRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer), pBuffer.readInt());
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, HoneyGeneratorRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
            pBuffer.writeInt(pRecipe.burnTime);
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
        return EsotericRegistration.HONEY_GENERATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.HONEY_GENERATOR_RECIPE.get();
    }
}
