package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.bees.Flower;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class FlowerRegistration {
    public static final ResourceKey<Registry<Flower>> FLOWER_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "flower"));

    public static final Codec<Flower> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().optionalFieldOf("blocks", new ArrayList<>()).forGetter(Flower::getBlocksAsResourceLocs),
                    TagKey.codec(ForgeRegistries.BLOCKS.getRegistryKey()).listOf().optionalFieldOf("tags", new ArrayList<>()).forGetter(flower -> flower.getFlowerTags().stream().toList())
            ).apply(instance, Flower::new)
    );
}
