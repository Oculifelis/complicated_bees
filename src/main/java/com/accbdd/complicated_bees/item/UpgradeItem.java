package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.MachineModifier;
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
    private final MachineModifier modifier;

    public UpgradeItem(Properties prop, MachineModifier modifier) {
        super(prop);
        this.modifier = modifier;
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
            if (this.getOutputMod() != 1)
                components.add(Component.translatable("item.complicated_bees.output_label")
                        .append(": ")
                        .append(Component.literal(this.getOutputMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
        }
    }

    public float getSpeedMod() {
        return modifier.getSpeedMod();
    }

    public float getEfficiencyMod() {
        return modifier.getEfficiencyMod();
    }

    public float getOutputMod() {
        return modifier.getOutputMod();
    }
}
