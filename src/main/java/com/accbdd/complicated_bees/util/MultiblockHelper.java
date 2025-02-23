package com.accbdd.complicated_bees.util;

import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MultiblockHelper {
    public static MellariumLogic tryBuildMellarium(Level level, BlockPos pos, @Nullable UUID owner) {
        if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity) {
            BlockPosBoxIterator centerIterator = new BlockPosBoxIterator(pos, 1, 1);
            while (centerIterator.hasNext()) {
                BlockPos testCenter = centerIterator.next();
                BlockPosBoxIterator structureIterator = new BlockPosBoxIterator(testCenter.offset(-1, -1, -1), testCenter.offset(1, 1, 1));
                boolean flag = true;
                while (structureIterator.hasNext() && flag) {
                    BlockPos structurePos = structureIterator.next();
                    if (level.getBlockEntity(structurePos) instanceof MellariumBaseBlockEntity mellariumBaseBlock) {
                        if (mellariumBaseBlock.getLogic() != null)
                            flag = false;
                    } else {
                        flag = false;
                    }
                }
                if (flag)
                    return new MellariumLogic(level, testCenter, owner);
            }
        }
        return null;
    }
}
