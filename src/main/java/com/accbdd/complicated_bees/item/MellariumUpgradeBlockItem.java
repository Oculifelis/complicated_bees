package com.accbdd.complicated_bees.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MellariumUpgradeBlockItem extends BlockItem {
    public final List<Component> tooltip;

    public MellariumUpgradeBlockItem(Block pBlock, Properties pProperties, List<Component> tooltip) {
        super(pBlock, pProperties);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.addAll(tooltip);
    }
}
