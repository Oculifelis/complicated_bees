package com.accbdd.complicated_bees.bees.mutation.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BlockTagUnderCondition extends MutationCondition {
    public static String ID = "block_tag_under";
    private final TagKey<Block> blockTag;

    public BlockTagUnderCondition(TagKey<Block> blockTag) {
        this.blockTag = blockTag;
    }

    @Override
    public ResourceLocation getID() {
        return ResourceLocation.fromNamespaceAndPath(MODID, ID);
    }

    @Override
    public boolean check(Level level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(blockTag);
    }

    @Override
    public Component getDescription() {
        return Component.translatable("gui.complicated_bees.mutations.block_under",
                Component.literal("#" + blockTag.location()));
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("tag", StringTag.valueOf(this.blockTag.location().toString()));
        return tag;
    }

    @Override
    public BlockTagUnderCondition deserialize(CompoundTag tag) {
        return new BlockTagUnderCondition(TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.tryParse(tag.getString("tag"))));
    }
}
