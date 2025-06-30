package com.accbdd.complicated_bees.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Objects;

public class Util {
    public static boolean canSeeSky(Level level, BlockPos pos) {
        if (level.canSeeSky(pos)) {
            //vanilla behaviour (check if sky light at position = max light in dimension)
            return true;
        } else if (level.dimensionType().hasCeiling()) { //dimension has a ceiling
            return false;
        } else if (!level.dimensionType().hasSkyLight()) { //no ceiling, but no sky light
            for (int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY(); y > pos.getY(); y--) {//loop through top block down, looking for skylight blocking blocks
                if (!level.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).propagatesSkylightDown(level, pos)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param match the compoundtag to match
     * @param pattern the pattern the compoundtag should match
     * @return whether all keys in pattern are equivalent to all keys in match
     */
    public static boolean weakNbtMatch(CompoundTag match, CompoundTag pattern) {
        for (String key : pattern.getAllKeys()) {
            if (!Objects.equals(match.get(key), pattern.get(key)))
                return false;
        }
        return true;
    }
}
