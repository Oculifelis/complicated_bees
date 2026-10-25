package com.accbdd.complicated_bees.datagen;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
public final class BlockTagGenerator {
    public static final TagKey<Block> SCOOPABLE = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "mineable/scoop_tool"));
    public static final TagKey<Block> MELLARIUM = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "multiblock/mellarium"));
    public static final TagKey<Block> GYROFUGE = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "multiblock/gyrofuge"));
    public static final TagKey<Block> DESERT_FLOWER = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "flowers/desert"));
}
