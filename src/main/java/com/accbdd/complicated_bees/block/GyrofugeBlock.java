package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GyrofugeBlock extends AbstractGyrofugeBlock {
    private final GyrofugeBlockType type;

    public GyrofugeBlock(Properties prop, GyrofugeBlockType type) {
        super(prop);
        this.type = type;
    }

    public GyrofugeBlock(GyrofugeBlockType type) {
        this(Properties.of()
                .mapColor(DyeColor.GRAY)
                .sound(SoundType.METAL)
                .strength(3.5f)
                .requiresCorrectToolForDrops(), type);
    }

    public GyrofugeBlock() {
        this(GyrofugeBlockType.OTHER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return switch (type) {
            case BASE -> new GyrofugeBaseBlockEntity(pPos, pState);
            case CONTROLLER -> new GyrofugeControllerBlockEntity(pPos, pState);
            default -> null;
        };
    }

    public enum GyrofugeBlockType {
        BASE,
        CONTROLLER,
        OTHER
    }
}
