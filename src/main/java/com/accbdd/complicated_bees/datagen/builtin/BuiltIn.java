package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
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
    public static final Map<ResourceLocation, MutationRecipe> MUTATIONS = new HashMap<>();

    public static Map.Entry<ResourceKey<Comb>, Comb> comb(String path, int outer, int inner) {
        Comb comb = new Comb(outer, inner);
        ResourceKey<Comb> key = ResourceKey.create(CombRegistration.COMB_REGISTRY_KEY, loc(path));
        COMBS.put(key, comb);
        return new AbstractMap.SimpleEntry<>(key, comb);
    }

    public static Map.Entry<ResourceKey<Flower>, Flower> flower(String path, List<Block> blocks, List<TagKey<Block>> tags) {
        List<ResourceLocation> blockLocations = blocks.stream().map(BuiltInRegistries.BLOCK::getKey).toList();
        Flower flower = new Flower(blockLocations, tags);
        ResourceKey<Flower> key = ResourceKey.create(FlowerRegistration.FLOWER_REGISTRY_KEY, loc(path));
        FLOWERS.put(key, flower);
        return new AbstractMap.SimpleEntry<>(key, flower);
    }

    public static Map.Entry<ResourceKey<Species>, Species> species(Species.Builder builder) {
        Species species = builder.build();
        ResourceKey<Species> key = ResourceKey.create(SpeciesRegistration.SPECIES_REGISTRY_KEY, species.builderOverride);
        SPECIES.put(key, species);
        return new AbstractMap.SimpleEntry<>(key, species);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ItemStack stack(Map.Entry<ResourceKey<Comb>, Comb> combEntry) {
        ItemStack stack = new ItemStack(ItemsRegistration.COMB.get(), 1);
        stack.set(EsotericRegistration.COMB_TYPE.get(), combEntry.getKey().location());
        return stack;
    }
}
