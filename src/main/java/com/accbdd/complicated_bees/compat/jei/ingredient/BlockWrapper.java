package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;

public record BlockWrapper(Block block) {
    public static MapCodec<BlockWrapper> CODEC = Block.CODEC.xmap(BlockWrapper::new, BlockWrapper::block);
}
