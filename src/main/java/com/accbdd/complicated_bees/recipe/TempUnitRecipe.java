package com.accbdd.complicated_bees.recipe;

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

public record TempUnitRecipe(Ingredient input, EnumTolerance tempChange, float useChance) implements Recipe<RecipeInput> {
    public static class Serializer implements RecipeSerializer<TempUnitRecipe> {
        public static final MapCodec<TempUnitRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(TempUnitRecipe::input),
                        EnumTolerance.CODEC.fieldOf("temp_change").forGetter(TempUnitRecipe::tempChange),
                        Codec.FLOAT.fieldOf("use_chance").forGetter(TempUnitRecipe::useChance)
                ).apply(instance, TempUnitRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, TempUnitRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<TempUnitRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TempUnitRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static TempUnitRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            return new TempUnitRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer), pBuffer.readEnum(EnumTolerance.class), pBuffer.readFloat());
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, TempUnitRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
            pBuffer.writeEnum(pRecipe.tempChange);
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
        return EsotericRegistration.TEMP_UNIT_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.TEMP_UNIT_RECIPE.get();
    }
}
