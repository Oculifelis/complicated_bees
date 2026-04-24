package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.block.GyrofugeEnergyCellBlock;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeEnergyCellBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class GyrofugeEnergyCellBlockItem extends BlockItem {
    public GyrofugeEnergyCellBlockItem() {
        super(BlocksRegistration.GYROFUGE_ENERGY_CELL.get(), new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.get(DataComponents.BLOCK_ENTITY_DATA) instanceof CustomData customData) {
            tooltipComponents.add(Component.translatable("gui.complicated_bees.energy_cell",
                    GyrofugeEnergyCellBlock.FORMAT.format(customData.getUnsafe().getInt(GyrofugeEnergyCellBlockEntity.ENERGY_TAG) / 1000D),
                    GyrofugeEnergyCellBlock.FORMAT.format(GyrofugeEnergyCellBlockEntity.BASE_STORAGE / 1000D)).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        if (pStack.get(DataComponents.BLOCK_ENTITY_DATA) instanceof CustomData customData) {
            return Math.round(customData.getUnsafe().getInt(GyrofugeEnergyCellBlockEntity.ENERGY_TAG) / (float) GyrofugeEnergyCellBlockEntity.BASE_STORAGE * 13);
        }
        return 0;
    }
}
