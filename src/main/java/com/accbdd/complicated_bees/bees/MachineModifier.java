package com.accbdd.complicated_bees.bees;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class MachineModifier {
    public static MachineModifier BLANK = new MachineModifier();

    private final float speedMod; //how fast the machine goes
    private final float efficiencyMod; //how much energy the machine uses
    private final float outputMod; //bonus to products
    private final int processingMod; //how many items the machine processes

    public MachineModifier(float speedMod, float efficiencyMod, float outputMod, int processingMod) {
        this.speedMod = speedMod;
        this.efficiencyMod = efficiencyMod;
        this.outputMod = outputMod;
        this.processingMod = processingMod;
    }

    public MachineModifier() {
        this(1, 1, 1, 1);
    }

    public static MachineModifier of(MachineModifier... modifiers) {
        float speedMod = 1;
        float efficiencyMod = 1;
        float outputMod = 1;
        int processingMod = 0;
        for (MachineModifier modifier : modifiers) {
            if (modifier == null)
                continue;
            speedMod *= modifier.speedMod;
            efficiencyMod *= modifier.efficiencyMod;
            outputMod *= modifier.outputMod;
            processingMod += modifier.processingMod;
        }

        return new MachineModifier(speedMod, efficiencyMod, outputMod, processingMod);
    }

    public float getEfficiencyMod() {
        return efficiencyMod;
    }

    public float getOutputMod() {
        return outputMod;
    }

    public float getSpeedMod() {
        return speedMod;
    }

    public int getProcessingMod() {
        return processingMod;
    }

    public List<Component> getTooltipComponents() {
        List<Component> components = new ArrayList<>();
        if (this.getSpeedMod() != 1)
            components.add(Component.translatable("upgrade.complicated_bees.speed_label")
                    .append(": ")
                    .append(Component.literal(String.format("%01.2fx", this.getSpeedMod())))
                    .withStyle(ChatFormatting.GRAY));
        if (this.getEfficiencyMod() != 1)
            components.add(Component.translatable("upgrade.complicated_bees.efficiency_label")
                    .append(": ")
                    .append(Component.literal(String.format("%01.2fx", this.getEfficiencyMod())))
                    .withStyle(ChatFormatting.GRAY));
        if (this.getOutputMod() != 1)
            components.add(Component.translatable("upgrade.complicated_bees.output_label")
                    .append(": ")
                    .append(Component.literal(String.format("%01.2fx", this.getOutputMod())))
                    .withStyle(ChatFormatting.GRAY));
        if (this.getProcessingMod() != 0)
            components.add(Component.translatable("upgrade.complicated_bees.processing_label")
                    .append(": ")
                    .append(Component.literal("+" + (this.getProcessingMod())))
                    .withStyle(ChatFormatting.GRAY));
        return components;
    }

    public static class Builder {
        private float speedMod = 1;
        private float efficiencyMod = 1;
        private float outputMod = 1;
        private int processingMod = 0;

        public MachineModifier build() {
            return new MachineModifier(speedMod, efficiencyMod, outputMod, processingMod);
        }

        public Builder speed(float mod) {
            this.speedMod = mod;
            return this;
        }

        public Builder efficiency(float mod) {
            this.efficiencyMod = mod;
            return this;
        }

        public Builder output(float mod) {
            this.outputMod = mod;
            return this;
        }

        public Builder processing(int mod) {
            this.processingMod = mod;
            return this;
        }
    }
}
