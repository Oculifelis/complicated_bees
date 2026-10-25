package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.block.MellariumEnergyCellBlock;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumEnergyCellBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class MellariumEnergyCellBlockItem extends BlockItem {
    public MellariumEnergyCellBlockItem() {
        super(BlocksRegistration.MELLARIUM_ENERGY_CELL.get(), com.accbdd.complicated_bees.port.RegistrationContext.itemProperties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.get(DataComponents.BLOCK_ENTITY_DATA) instanceof net.minecraft.world.item.component.TypedEntityData<?> customData) {
            tooltipComponents.accept(Component.translatable("gui.complicated_bees.energy_cell",
                    MellariumEnergyCellBlock.FORMAT.format(customData.getUnsafe().getIntOr(MellariumEnergyCellBlockEntity.ENERGY_TAG, 0) / 1000D),
                    MellariumEnergyCellBlock.FORMAT.format(MellariumEnergyCellBlockEntity.BASE_STORAGE / 1000D)).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        if (pStack.get(DataComponents.BLOCK_ENTITY_DATA) instanceof net.minecraft.world.item.component.TypedEntityData<?> customData) {
            return Math.round(customData.getUnsafe().getIntOr(MellariumEnergyCellBlockEntity.ENERGY_TAG, 0) / (float) MellariumEnergyCellBlockEntity.BASE_STORAGE * 13);
        }
        return 0;
    }
}
