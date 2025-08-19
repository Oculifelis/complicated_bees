package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.bees.MachineModifier;
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
        super.appendHoverText(pStack, pLevel, components, pIsAdvanced);
        components.addAll(modifier.getTooltipComponents());
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

    public float getProcessingMod() {
        return modifier.getProcessingMod();
    }

    public MachineModifier getModifier() {
        return modifier;
    }
}
