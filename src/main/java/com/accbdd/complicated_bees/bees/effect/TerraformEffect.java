package com.accbdd.complicated_bees.bees.effect;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.gene.GeneTerritory;
import com.accbdd.complicated_bees.util.BlockPosSpiralIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.Random;

public class TerraformEffect extends BeeEffect {
    private static Random rand = new Random();
    private ResourceKey<Biome> biome;

    public TerraformEffect(ResourceKey<Biome> biome) {
        this.biome = biome;
    }

    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (cycleProgress == 0 && rand.nextFloat() < 20.02f) {
            int[] territory = (int[]) GeneticHelper.getGeneValue(queen, GeneTerritory.ID, true);
            ServerLevel level = (ServerLevel) apiary.getLevel();
            BlockPosSpiralIterator iterator = new BlockPosSpiralIterator(apiary.getBlockPos(), territory[0], territory[1]);
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                if (pos.getY() - 1 > apiary.getLevel().getMinBuildHeight() & !apiary.getLevel().getBiome(quantize(pos)).is(biome)) {
                    ChunkAccess chunkaccess = level.getChunk(pos);
                    if (chunkaccess != null) {
                        BoundingBox boundingBox = BoundingBox.fromCorners(quantize(pos.offset(-2, -2, -2)), quantize(pos.offset(2, 2, 2)));
                        chunkaccess.fillBiomesFromNoise(makeResolver(chunkaccess,
                                boundingBox,
                                GeneticHelper.getRegistryAccess().registryOrThrow(Registries.BIOME).getHolder(biome).orElseThrow()), level.getChunkSource().randomState().sampler());
                        chunkaccess.setUnsaved(true);
                        if (apiary.getLevel().getBiome(quantize(pos)).is(biome)) {
                            level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunkaccess));
                            break;
                        }
                    }
                }
            }
        }
    }

    //straight ripped from fillbiome command
    private static BiomeResolver makeResolver(ChunkAccess pChunk, BoundingBox pTargetRegion, Holder<Biome> pReplacementBiome) {
        return (x, y, z, sampler) -> {
            int i = QuartPos.toBlock(x);
            int j = QuartPos.toBlock(y);
            int k = QuartPos.toBlock(z);
            Holder<Biome> holder = pChunk.getNoiseBiome(x, y, z);
            if (pTargetRegion.isInside(i, j, k)) {
                return pReplacementBiome;
            } else {
                return holder;
            }
        };
    }

    private static int quantize(int pValue) {
        return QuartPos.toBlock(QuartPos.fromBlock(pValue));
    }

    private static BlockPos quantize(BlockPos pPos) {
        return new BlockPos(quantize(pPos.getX()), quantize(pPos.getY()), quantize(pPos.getZ()));
    }
}
