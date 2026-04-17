package com.accbdd.complicated_bees.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class MellariumUpgradeBlockItem extends BlockItem {
    public final List<Component> tooltip;

    public MellariumUpgradeBlockItem(Block pBlock, List<Component> tooltip) {
        super(pBlock, new Properties());
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.addAll(tooltip);
    }
}
