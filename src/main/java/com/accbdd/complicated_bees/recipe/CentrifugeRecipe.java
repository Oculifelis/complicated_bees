package com.accbdd.complicated_bees.recipe;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record CentrifugeRecipe(Ingredient input, List<Product> outputs) implements Recipe<RecipeInput> {
    public static class Serializer implements RecipeSerializer<CentrifugeRecipe> {
        private static final MapCodec<CentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> 
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(CentrifugeRecipe::input),
                        Product.CODEC.listOf().fieldOf("outputs").forGetter(CentrifugeRecipe::outputs)
                ).apply(instance, CentrifugeRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<CentrifugeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CentrifugeRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            List<Product> outputs = new ArrayList<>();
            int listSize = pBuffer.readInt();
            for (int i = 0; i < listSize; i++) {
                outputs.add(Product.fromNetwork(pBuffer));
            }
            return new CentrifugeRecipe(input, outputs);
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, CentrifugeRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.input);
            pBuffer.writeInt(pRecipe.outputs.size());
            for (Product prod : pRecipe.outputs) {
                prod.toNetwork(pBuffer);
            }
        }
    }

    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        ItemStack containerInput = pContainer.getItem(0);
        return input.test(containerInput);
    }

    // use outputs instead
    @Override
    public ItemStack assemble(RecipeInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 1;
    }

    // use outputs instead
    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EsotericRegistration.CENTRIFUGE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.CENTRIFUGE_RECIPE.get();
    }


}
