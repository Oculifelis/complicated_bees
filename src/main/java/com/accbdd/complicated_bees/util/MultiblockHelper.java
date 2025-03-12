package com.accbdd.complicated_bees.util;

import com.accbdd.complicated_bees.block.entity.MellariumAbstractBlockEntity;
import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MultiblockHelper {
    public static MellariumLogic tryBuildMellarium(Level level, BlockPos pos, @Nullable UUID owner) {
        if (level.getBlockEntity(pos) instanceof MellariumAbstractBlockEntity) {
            BlockPosBoxIterator centerIterator = new BlockPosBoxIterator(pos, 1, 1);
            while (centerIterator.hasNext()) {
                BlockPos testCenter = centerIterator.next();
                if (isValidMellarium(level, testCenter)) {
                    return buildMellarium(level, testCenter, owner);
                }
            }
        }
        return null;
    }

    public static boolean isValidMellarium(Level level, BlockPos center) {
        if (!(level.getBlockEntity(center) instanceof MellariumBaseBlockEntity || level.getBlockEntity(center) instanceof MellariumControllerBlockEntity))
            return false;
        BlockPosBoxIterator structureIterator = new BlockPosBoxIterator(center.offset(-1, -1, -1), center.offset(1, 1, 1));
        while (structureIterator.hasNext()) {
            BlockPos structurePos = structureIterator.next();
            if (level.getBlockEntity(structurePos) instanceof MellariumAbstractBlockEntity mellariumBlock) {
                if (mellariumBlock.getLogic() != null) {
                    return false;
                }
            } else {
                if (!(level.getBlockEntity(structurePos) instanceof MellariumControllerBlockEntity) || !structurePos.equals(center)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static MellariumLogic buildMellarium(Level level, BlockPos center, UUID owner) {
        level.setBlockAndUpdate(center, BlocksRegistration.MELLARIUM_CONTROLLER.get().defaultBlockState());
        return new MellariumLogic(level, center, owner);
    }
}
