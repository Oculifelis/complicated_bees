package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.block.AbstractGyrofugePoweredBlock;
import com.accbdd.complicated_bees.block.entity.gyrofuge.AbstractGyrofugePoweredModifierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class GyrofugeUpgradeBlockItem extends BlockItem {
    public List<Component> tooltip;

    public GyrofugeUpgradeBlockItem(Block pBlock) {
        super(pBlock, new Properties());
        this.tooltip = List.of();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (this.tooltip.isEmpty() && getBlock() instanceof AbstractGyrofugePoweredBlock block && block.newBlockEntity(BlockPos.ZERO, block.defaultBlockState()) instanceof AbstractGyrofugePoweredModifierBlockEntity be) {
            this.tooltip = be.getTooltip();
        }
        tooltipComponents.addAll(tooltip);
    }
}
