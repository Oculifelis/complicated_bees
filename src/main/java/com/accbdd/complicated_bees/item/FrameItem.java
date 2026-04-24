package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class FrameItem extends DisableableItem {
    private final BeeHousingModifier modifier;

    public FrameItem(Properties pProperties, BeeHousingModifier modifier, ModConfigSpec.ConfigValue<Boolean> configValue) {
        super(pProperties, configValue);
        this.modifier = modifier;
    }

    public BeeHousingModifier getModifier() {
        return modifier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context.level() != null) {
            tooltipComponents.addAll(modifier.getTooltipComponent());
        }
    }
}

