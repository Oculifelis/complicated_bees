package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Flower;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

import static com.accbdd.complicated_bees.datagen.builtin.BuiltIn.flower;

public class Flowers {
    public static final Map.Entry<ResourceKey<Flower>, Flower> CALCITE = flower("calcite", List.of(Blocks.CALCITE), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> CHORUS = flower("chorus", List.of(Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DEBRIS = flower("debris", List.of(Blocks.ANCIENT_DEBRIS), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DEEPSLATE = flower("deepslate", List.of(Blocks.DEEPSLATE), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DESERT = flower("desert", List.of(Blocks.CACTUS, Blocks.DEAD_BUSH), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DIORITE = flower("diorite", List.of(Blocks.DIORITE), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DRAGON_EGG = flower("dragon_egg", List.of(Blocks.DRAGON_EGG), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> DRIPSTONE = flower("dripstone", List.of(Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> FLOWER = flower("flower", List.of(), List.of(BlockTags.FLOWERS));
    public static final Map.Entry<ResourceKey<Flower>, Flower> GRANITE = flower("granite", List.of(Blocks.GRANITE), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> JUNGLE = flower("jungle", List.of(Blocks.VINE, Blocks.COCOA, Blocks.MELON, Blocks.BAMBOO, Blocks.BAMBOO_SAPLING), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> LUSH_CAVE = flower("lush_cave", List.of(Blocks.FLOWERING_AZALEA, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.SPORE_BLOSSOM, Blocks.SMALL_DRIPLEAF), List.of(BlockTags.CAVE_VINES));
    public static final Map.Entry<ResourceKey<Flower>, Flower> MUSICAL = flower("musical", List.of(Blocks.NOTE_BLOCK, Blocks.JUKEBOX), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> NETHER = flower("nether", List.of(Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.NETHER_SPROUTS, Blocks.CRIMSON_ROOTS, Blocks.WARPED_ROOTS, Blocks.SHROOMLIGHT, Blocks.NETHER_WART), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> SKULLS = flower("skulls", List.of(Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL), List.of());
    public static final Map.Entry<ResourceKey<Flower>, Flower> TUFF = flower("tuff", List.of(Blocks.TUFF), List.of());
}
