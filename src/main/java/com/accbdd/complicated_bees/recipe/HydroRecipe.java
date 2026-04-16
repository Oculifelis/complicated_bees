package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
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

public record HydroRecipe(Ingredient input, Product output, EnumTolerance humidityChange, float useChance) implements Recipe<RecipeInput> {
    public static class Serializer implements RecipeSerializer<HydroRecipe> {
        public static final MapCodec<HydroRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(HydroRecipe::input),
                        Product.CODEC.fieldOf("output").forGetter(HydroRecipe::output),
                        EnumTolerance.CODEC.fieldOf("humidity_change").forGetter(HydroRecipe::humidityChange),
                        Codec.FLOAT.fieldOf("use_chance").forGetter(HydroRecipe::useChance)
                ).apply(instance, HydroRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, HydroRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<HydroRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HydroRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static HydroRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            return new HydroRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer), Product.fromNetwork(pBuffer), pBuffer.readEnum(EnumTolerance.class), pBuffer.readFloat());
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, HydroRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
            pRecipe.output().toNetwork(pBuffer);
            pBuffer.writeEnum(pRecipe.humidityChange);
            pBuffer.writeFloat(pRecipe.useChance);
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
        return EsotericRegistration.HYDROREGULATOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.HYDROREGULATOR_RECIPE.get();
    }
}
