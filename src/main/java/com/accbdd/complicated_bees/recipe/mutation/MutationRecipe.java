package com.accbdd.complicated_bees.recipe.mutation;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.recipe.mutation.condition.IMutationCondition;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MutationRecipe implements Recipe<MutationRecipeInput> {
    private final ResourceLocation first, second, result;
    private final float chance;
    private final List<IMutationCondition> conditions;

    public MutationRecipe(ResourceLocation first, ResourceLocation second, ResourceLocation result, float chance, List<IMutationCondition> conditions) {
        this.first = first;
        this.second = second;
        this.result = result;
        this.chance = chance;
        this.conditions = conditions;
    }

    public MutationRecipe(ResourceLocation first, ResourceLocation second, ResourceLocation result, float chance, CompoundTag conditions) {
        this(first, second, result, chance, new ArrayList<>());
        List<IMutationCondition> list = getConditions();
        for (String key : conditions.getAllKeys()) {
            IMutationCondition condition = ComplicatedBees.MUTATION_CONDITION_REGISTRY.get().get(ResourceLocation.tryParse(key));
            if (condition != null)
                list.add(condition.deserialize(conditions.getCompound(key)));
            else
                ComplicatedBees.LOGGER.error("could not find condition {}", key);
        }
    }

    public ResourceLocation getFirst() {
        return first;
    }

    public Species getFirstSpecies() {
        return SpeciesRegistration.getFromResourceLocation(first);
    }

    public ResourceLocation getSecond() {
        return second;
    }

    public Species getSecondSpecies() {
        return SpeciesRegistration.getFromResourceLocation(second);
    }

    public ResourceLocation getResult() {
        return result;
    }

    public Species getResultSpecies() {
        return SpeciesRegistration.getFromResourceLocation(result);
    }

    public float getChance() {
        return chance;
    }

    public List<IMutationCondition> getConditions() {
        return conditions;
    }

    public static CompoundTag getSerializedConditions(List<IMutationCondition> conditions) {
        CompoundTag tag = new CompoundTag();
        for (IMutationCondition condition : conditions) {
            ResourceLocation loc = condition.getID();
            if (loc == null) {
                ComplicatedBees.LOGGER.error("tried to serialize mutation with no id! description: {}", condition.getDescription());
            } else {
                tag.put(loc.toString(), condition.serialize());
            }
        }
        return tag;
    }

    @Override
    public boolean matches(MutationRecipeInput input, Level level) {
        return (input.first().equals(this.first) && input.second().equals(this.second)) || (input.first().equals(this.second) && input.second().equals(this.first));
    }

    @Override
    public ItemStack assemble(MutationRecipeInput mutationRecipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EsotericRegistration.MUTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return EsotericRegistration.MUTATION_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<MutationRecipe> {
        public static final MapCodec<MutationRecipe> MUTATION_CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("first").forGetter(MutationRecipe::getFirst),
                        ResourceLocation.CODEC.fieldOf("second").forGetter(MutationRecipe::getSecond),
                        ResourceLocation.CODEC.fieldOf("result").forGetter(MutationRecipe::getResult),
                        Codec.FLOAT.fieldOf("chance").forGetter(MutationRecipe::getChance),
                        CompoundTag.CODEC.optionalFieldOf("conditions", new CompoundTag()).forGetter(mutation -> MutationRecipe.getSerializedConditions(mutation.getConditions()))
                ).apply(instance, MutationRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, MutationRecipe> MUTATION_STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, MutationRecipe::getFirst,
                ResourceLocation.STREAM_CODEC, MutationRecipe::getSecond,
                ResourceLocation.STREAM_CODEC, MutationRecipe::getResult,
                ByteBufCodecs.FLOAT, MutationRecipe::getChance,
                ByteBufCodecs.COMPOUND_TAG, mutation -> MutationRecipe.getSerializedConditions(mutation.getConditions()),
                MutationRecipe::new
        );

        @Override
        public MapCodec<MutationRecipe> codec() {
            return MUTATION_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MutationRecipe> streamCodec() {
            return MUTATION_STREAM_CODEC;
        }
    }
}
