package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.bees.mutation.condition.IMutationCondition;
import com.accbdd.complicated_bees.item.CombItem;
import com.accbdd.complicated_bees.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BuiltIn {
    public static final Map<ResourceKey<Comb>, Comb> COMBS = new HashMap<>();
    public static final Map<ResourceKey<Flower>, Flower> FLOWERS = new HashMap<>();
    public static final Map<ResourceKey<Species>, Species> SPECIES = new HashMap<>();
    public static final Map<ResourceKey<Mutation>, Mutation> MUTATIONS = new HashMap<>();

    static Map.Entry<ResourceKey<Comb>, Comb> comb(String path, int outer, int inner) {
        Comb comb = new Comb(outer, inner);
        ResourceKey<Comb> key = ResourceKey.create(CombRegistration.COMB_REGISTRY_KEY, loc(path));
        COMBS.put(key, comb);
        return new AbstractMap.SimpleEntry<>(key, comb);
    }

    static Map.Entry<ResourceKey<Flower>, Flower> flower(String path, List<Block> blocks, List<TagKey<Block>> tags) {
        List<ResourceLocation> blockLocations = blocks.stream().map(BuiltInRegistries.BLOCK::getKey).toList();
        Flower flower = new Flower(blockLocations, tags);
        ResourceKey<Flower> key = ResourceKey.create(FlowerRegistration.FLOWER_REGISTRY_KEY, loc(path));
        FLOWERS.put(key, flower);
        return new AbstractMap.SimpleEntry<>(key, flower);
    }

    static Map.Entry<ResourceKey<Species>, Species> species(Species.Builder builder) {
        Species species = builder.build();
        ResourceKey<Species> key = ResourceKey.create(SpeciesRegistration.SPECIES_REGISTRY_KEY, species.builderOverride);
        SPECIES.put(key, species);
        return new AbstractMap.SimpleEntry<>(key, species);
    }

    static Map.Entry<ResourceKey<Mutation>, Mutation> mutation(String path, ResourceKey<Species> first, ResourceKey<Species> second, ResourceKey<Species> result, float chance, List<IMutationCondition> conditions) {
        Mutation mutation = new Mutation(first.location(), second.location(), result.location(), chance, conditions);
        ResourceKey<Mutation> key = ResourceKey.create(MutationRegistration.MUTATION_REGISTRY_KEY, loc(path));
        MUTATIONS.put(key, mutation);
        return new AbstractMap.SimpleEntry<>(key, mutation);
    }

    static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }

    static ItemStack stack(Map.Entry<ResourceKey<Comb>, Comb> combEntry) {
        ItemStack stack = new ItemStack(ItemsRegistration.COMB.get(), 1);
        CompoundTag tag = new CompoundTag();
        tag.putString(CombItem.COMB_TYPE_TAG, combEntry.getKey().location().toString());
        stack.setTag(tag);
        return stack;
    }
}
