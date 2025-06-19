package com.accbdd.complicated_bees.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeItem extends Item {
    private final float speedMod;
    private final float efficiencyMod;

    public UpgradeItem(Properties prop, float speedMod, float efficiencyMod) {
        super(prop);
        this.speedMod = speedMod;
        this.efficiencyMod = efficiencyMod;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        if (Minecraft.getInstance().level != null) {
            if (this.getSpeedMod() != 1)
                components.add(Component.translatable("item.complicated_bees.speed_label")
                        .append(": ")
                        .append(Component.literal(this.getSpeedMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
            if (this.getEfficiencyMod() != 1)
                components.add(Component.translatable("item.complicated_bees.efficiency_label")
                        .append(": ")
                        .append(Component.literal(this.getEfficiencyMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
        }
    }

    public float getSpeedMod() {
        return speedMod;
    }

    public float getEfficiencyMod() {
        return efficiencyMod;
    }
}
