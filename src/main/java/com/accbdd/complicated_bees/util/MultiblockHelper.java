package com.accbdd.complicated_bees.util;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumAbstractBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
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
        if (level == null || !(level.getBlockEntity(center) instanceof MellariumBaseBlockEntity || level.getBlockEntity(center) instanceof MellariumControllerBlockEntity))
            return false;
        BlockPosBoxIterator structureIterator = new BlockPosBoxIterator(center.offset(-1, -1, -1), center.offset(1, 1, 1));
        while (structureIterator.hasNext()) {
            BlockPos structurePos = structureIterator.next();
            if (level.getBlockEntity(structurePos) instanceof MellariumAbstractBlockEntity mellariumBlock) {
                if (mellariumBlock.getLogic() != null && !level.isClientSide()) {
                    //todo: dear god, fix this garbage !isClientSide call
                    return false;
                } else {
                    if (!(mellariumBlock instanceof MellariumBaseBlockEntity) && structurePos.getY() > center.getY()) //only allow non-base blocks in the bottom two layers
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
        level.setBlockAndUpdate(center, BlocksRegistration.MELLARIUM_CONTROLLER.get().defaultBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side));
        return new MellariumLogic(level, center, owner);
    }
}
