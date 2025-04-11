package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.CombRegistration;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import com.accbdd.complicated_bees.registry.GeneRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BuiltIn {
    public static final Map<ResourceKey<Comb>, Comb> COMBS = new HashMap<>();
    public static final Map<ResourceKey<Flower>, Flower> FLOWERS = new HashMap<>();
    public static final Map<ResourceKey<Species>, Species> SPECIES = new HashMap<>();

    public static final Comb AMETHYST = comb("amethyst", 0xafad9c, 0xa86df9);
    public static final Comb COAL = comb("coal", 0xafad9c, 0x3f3f3f);
    public static final Comb COPPER = comb("copper", 0xafad9c, 0xd37a5a);
    public static final Comb DIAMOND = comb("diamond", 0xafad9c, 0x79caec);
    public static final Comb DRIPPING = comb("dripping", 0xd68400, 0xfff700);
    public static final Comb DUSTY = comb("dusty", 0xe4d169, 0xccad50);
    public static final Comb EMERALD = comb("emerald", 0xafad9c, 0x3ad261);
    public static final Comb GLOWSTONE = comb("glowstone", 0x652828, 0xcbcd0a);
    public static final Comb GOLD = comb("gold", 0xafad9c, 0xe4d23b);
    public static final Comb HONEY = comb("honey", 0xe7d46a, 0xfea02b);
    public static final Comb IRON = comb("iron", 0xafad9c, 0xdbdbdb);
    public static final Comb LAPIS = comb("lapis", 0xafad9c, 0x1815a2);
    public static final Comb MYSTERIOUS = comb("mysterious", 0xf4ef62, 0xc262f4);
    public static final Comb NETHERITE = comb("netherite", 0x3b3224, 0x3b3224);
    public static final Comb QUARTZ = comb("quartz", 0x652828, 0xc8c4ae);
    public static final Comb REDSTONE = comb("redstone", 0xafad9c, 0xa31300);
    public static final Comb ROCKY = comb("rocky", 0xafad9c, 0xafa87e);
    public static final Comb ROTTEN = comb("rotten", 0x655f00, 0x443507);
    public static final Comb ROYAL = comb("royal",0xa71400, 0xffcc3e);
    public static final Comb SILKY = comb("silky", 0x5a820e, 0xf6ec21);
    public static final Comb SIMMERING = comb("simmering", 0x652828, 0xfa5200);
    public static final Comb SPECTRAL = comb("spectral", 0xfffd68, 0xffffff);
    public static final Comb STRINGY = comb("stringy", 0xe8d880, 0xfcb968);

    public static final Flower CALCITE = flower("calcite", List.of(Blocks.CALCITE), List.of());
    public static final Flower CHORUS = flower("chorus", List.of(Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT), List.of());
    public static final Flower DEBRIS = flower("debris", List.of(Blocks.ANCIENT_DEBRIS), List.of());
    public static final Flower DEEPSLATE = flower("deepslate", List.of(Blocks.DEEPSLATE), List.of());
    public static final Flower DESERT = flower("desert", List.of(Blocks.CACTUS, Blocks.DEAD_BUSH), List.of());
    public static final Flower DIORITE = flower("diorite", List.of(Blocks.DIORITE), List.of());
    public static final Flower DRIPSTONE = flower("dripstone", List.of(Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE), List.of());
    public static final Flower FLOWER = flower("flower", List.of(), List.of(BlockTags.FLOWERS));
    public static final Flower GRANITE = flower("granite", List.of(Blocks.GRANITE), List.of());
    public static final Flower JUNGLE = flower("jungle", List.of(Blocks.VINE, Blocks.COCOA, Blocks.MELON, Blocks.BAMBOO, Blocks.BAMBOO_SAPLING), List.of());
    public static final Flower LUSH_CAVE = flower("lush_cave", List.of(Blocks.FLOWERING_AZALEA, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.SPORE_BLOSSOM, Blocks.SMALL_DRIPLEAF), List.of(BlockTags.CAVE_VINES));
    public static final Flower NETHER = flower("nether", List.of(Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.NETHER_SPROUTS, Blocks.CRIMSON_ROOTS, Blocks.WARPED_ROOTS, Blocks.SHROOMLIGHT, Blocks.NETHER_WART), List.of());
    public static final Flower SKULLS = flower("skulls", List.of(Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.PIGLIN_HEAD, Blocks.PIGLIN_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL), List.of());
    public static final Flower TUFF = flower("tuff", List.of(Blocks.TUFF), List.of());

    public static final Species TEST = species(new Species.Builder(loc("debug/real_test"))
            .colors(0x0000FF, 0x0000FF)
            .gene(GeneRegistration.TERRITORY.get(), new int[]{1, 2}, false));

    private static Comb comb(String path, int outer, int inner) {
        Comb comb = new Comb(outer, inner);
        COMBS.put(ResourceKey.create(CombRegistration.COMB_REGISTRY_KEY, new ResourceLocation(MODID, path)), comb);
        return comb;
    }

    private static Flower flower(String path, List<Block> blocks, List<TagKey<Block>> tags) {
        List<ResourceLocation> blockLocations = blocks.stream().map(BuiltInRegistries.BLOCK::getKey).toList();
        Flower flower = new Flower(blockLocations, tags);
        FLOWERS.put(ResourceKey.create(FlowerRegistration.FLOWER_REGISTRY_KEY, new ResourceLocation(MODID, path)), flower);
        return flower;
    }

    private static Species species(Species.Builder builder) {
        Species species = builder.build();
        SPECIES.put(ResourceKey.create(SpeciesRegistration.SPECIES_REGISTRY_KEY, species.builderOverride), species);
        return species;
    }

    private static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
