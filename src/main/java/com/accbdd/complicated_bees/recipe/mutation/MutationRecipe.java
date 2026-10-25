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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MutationRecipe implements com.accbdd.complicated_bees.port.MachineRecipe<MutationRecipeInput> {
    private final Identifier first, second, result;
    private final float chance;
    private final List<IMutationCondition> conditions;

    public MutationRecipe(Identifier first, Identifier second, Identifier result, float chance, List<IMutationCondition> conditions) {
        this.first = first;
        this.second = second;
        this.result = result;
        this.chance = chance;
        this.conditions = conditions;
    }

    public MutationRecipe(Identifier first, Identifier second, Identifier result, float chance, CompoundTag conditions) {
        this(first, second, result, chance, new ArrayList<>());
        List<IMutationCondition> list = getConditions();
        for (String key : conditions.keySet()) {
            IMutationCondition condition = ComplicatedBees.MUTATION_CONDITION_REGISTRY.get().getValue(Identifier.tryParse(key));
            if (condition != null)
                list.add(condition.deserialize(conditions.getCompoundOrEmpty(key)));
            else
                ComplicatedBees.LOGGER.error("could not find condition {}", key);
        }
    }

    public Identifier getFirst() {
        return first;
    }

    public Species getFirstSpecies() {
        return SpeciesRegistration.getFromIdentifier(first);
    }

    public Identifier getSecond() {
        return second;
    }

    public Species getSecondSpecies() {
        return SpeciesRegistration.getFromIdentifier(second);
    }

    public Identifier getResult() {
        return result;
    }

    public Species getResultSpecies() {
        return SpeciesRegistration.getFromIdentifier(result);
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
            Identifier loc = condition.getID();
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
    public RecipeSerializer<? extends Recipe<MutationRecipeInput>> getSerializer() {
        return EsotericRegistration.MUTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<MutationRecipeInput>> getType() {
        return EsotericRegistration.MUTATION_RECIPE.get();
    }

    public static class Serializer {
        public static final MapCodec<MutationRecipe> MUTATION_CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Identifier.CODEC.fieldOf("first").forGetter(MutationRecipe::getFirst),
                        Identifier.CODEC.fieldOf("second").forGetter(MutationRecipe::getSecond),
                        Identifier.CODEC.fieldOf("result").forGetter(MutationRecipe::getResult),
                        Codec.FLOAT.fieldOf("chance").forGetter(MutationRecipe::getChance),
                        CompoundTag.CODEC.optionalFieldOf("conditions", new CompoundTag()).forGetter(mutation -> MutationRecipe.getSerializedConditions(mutation.getConditions()))
                ).apply(instance, MutationRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, MutationRecipe> MUTATION_STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, MutationRecipe::getFirst,
                Identifier.STREAM_CODEC, MutationRecipe::getSecond,
                Identifier.STREAM_CODEC, MutationRecipe::getResult,
                ByteBufCodecs.FLOAT, MutationRecipe::getChance,
                ByteBufCodecs.COMPOUND_TAG, mutation -> MutationRecipe.getSerializedConditions(mutation.getConditions()),
                MutationRecipe::new
        );

        public MapCodec<MutationRecipe> codec() {
            return MUTATION_CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, MutationRecipe> streamCodec() {
            return MUTATION_STREAM_CODEC;
        }
    }
}
