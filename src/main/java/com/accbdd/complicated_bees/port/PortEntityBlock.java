package com.accbdd.complicated_bees.port;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
public abstract class PortEntityBlock extends BaseEntityBlock {
    protected PortEntityBlock(Properties properties){super(properties);}
    public void onRemove(BlockState oldState,Level level,BlockPos pos,BlockState newState,boolean movedByPiston){}
}
