package com.accbdd.complicated_bees.recipe.mutation.condition;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BiomeCondition extends MutationCondition {
    public static final String ID = "biome";
    private ResourceKey<Biome> biome;
    private TagKey<Biome> biomeTag;

    public BiomeCondition(ResourceKey<Biome> biome) {
        this.biome = biome;
    }

    public BiomeCondition(TagKey<Biome> biomeTag) {
        this.biomeTag = biomeTag;
    }

    @Override
    public Identifier getID() {
        return Identifier.fromNamespaceAndPath(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        return level.getBiome(pos).is(biomeTag) || level.getBiome(pos).is(biome);
    }

    @Override
    public Component getDescription() {
        if (biomeTag == null)
            return Component.translatable("gui.complicated_bees.mutations.biome", Component.translatable(biome.identifier().toLanguageKey("biome")).withStyle(ChatFormatting.GOLD));
        return Component.translatable("gui.complicated_bees.mutations.biome_tag", Component.literal("#").withStyle(ChatFormatting.GOLD).append(Component.literal(biomeTag.location().toString()).withStyle(ChatFormatting.GOLD)));
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("biome", biomeTag == null ? StringTag.valueOf(biome.identifier().toString()) : StringTag.valueOf("#"+biomeTag.location()));
        return tag;
    }

    @Override
    public IMutationCondition deserialize(CompoundTag tag) {
        String data = tag.getStringOr("biome", "");
        if (data.startsWith("#"))
            return new BiomeCondition(new TagKey<>(Registries.BIOME, Identifier.tryParse(data.substring(1))));
        else
            return new BiomeCondition(ResourceKey.create(Registries.BIOME, Identifier.tryParse(data)));
    }
}
