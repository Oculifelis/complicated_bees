package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MellariumBlock extends AbstractMellariumBlock {
    public static final String SCREEN_MELLARIUM = "gui.complicated_bees.mellarium";
    private final MellariumBlockType type;

    public MellariumBlock(Properties prop, MellariumBlockType type) {
        super(prop);
        this.type = type;
    }

    public MellariumBlock(MellariumBlockType type) {
        this(Properties.of().mapColor(DyeColor.ORANGE).sound(SoundType.WOOD).strength(1), type);
    }

    public MellariumBlock() {
        this(MellariumBlockType.OTHER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return switch (type) {
            case BASE -> new MellariumBaseBlockEntity(pPos, pState);
            case CONTROLLER -> new MellariumControllerBlockEntity(pPos, pState);
            default -> null;
        };
    }

    public enum MellariumBlockType {
        BASE,
        CONTROLLER,
        OTHER
    }
}
