package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BeeHousingModifier {
    private final EnumTolerance temperatureMod;
    private final EnumTolerance humidityMod;
    private final float lifespanMod;
    private final float productivityMod;
    private final float territoryMod;
    private final float mutationMod;
    private final boolean skyOverride;
    private final boolean rainOverride;
    private final boolean sleepOverride;

    public static BeeHousingModifier of(BeeHousingModifier... modifiers) {
        EnumTolerance temperatureMod = EnumTolerance.NONE;
        EnumTolerance humidityMod = EnumTolerance.NONE;
        float lifespanMod = 1;
        float productivityMod = 1;
        float territoryMod = 1;
        float mutationMod = 1;
        boolean skyOverride = false;
        boolean rainOverride = false;
        boolean sleepOverride = false;
        for (BeeHousingModifier modifier : modifiers) {
            temperatureMod = EnumTolerance.getFromCollapsed(temperatureMod.collapsed() + modifier.temperatureMod.collapsed());
            humidityMod = EnumTolerance.getFromCollapsed(humidityMod.collapsed() + modifier.humidityMod.collapsed());
            lifespanMod *= modifier.lifespanMod;
            productivityMod *= modifier.productivityMod;
            territoryMod *= modifier.territoryMod;
            mutationMod *= modifier.mutationMod;
            skyOverride = skyOverride||modifier.skyOverride;
            rainOverride = rainOverride||modifier.rainOverride;
            sleepOverride = sleepOverride||modifier.sleepOverride;
        }

        return new BeeHousingModifier(temperatureMod, humidityMod, lifespanMod, productivityMod, territoryMod, mutationMod, skyOverride, rainOverride, sleepOverride);
    }

    public BeeHousingModifier(EnumTolerance temperatureMod, EnumTolerance humidityMod, float lifespanMod, float productivityMod, float territoryMod, float mutationMod, boolean skyOverride, boolean rainOverride, boolean sleepOverride) {
        this.temperatureMod = temperatureMod;
        this.humidityMod = humidityMod;
        this.lifespanMod = lifespanMod;
        this.productivityMod = productivityMod;
        this.territoryMod = territoryMod;
        this.mutationMod = mutationMod;
        this.skyOverride = skyOverride;
        this.rainOverride = rainOverride;
        this.sleepOverride = sleepOverride;
    }

    public BeeHousingModifier() {
        this(
            EnumTolerance.NONE,
            EnumTolerance.NONE,
            1,
            1,
            1,
            1,
            false,
            false,
            false
        );
    }

    public EnumTolerance getTemperatureMod() {
        return temperatureMod;
    }

    public EnumTolerance getHumidityMod() {
        return humidityMod;
    }

    public float getLifespanMod() {
        return lifespanMod;
    }

    public float getProductivityMod() {
        return productivityMod;
    }

    public float getTerritoryMod() {
        return territoryMod;
    }

    public float getMutationMod() {
        return mutationMod;
    }

    public boolean getSkyOverride() {
        return skyOverride;
    }

    public boolean getRainOverride() {
        return rainOverride;
    }

    public boolean getSleepOverride() {
        return sleepOverride;
    }

    public List<Component> getTooltipComponent() {
        List<Component> components = new ArrayList<>();
        if (Minecraft.getInstance().level != null) {
            if (this.getLifespanMod() != 1)
                components.add(Component.translatable("gene.complicated_bees.lifespan_label.short")
                        .append(": ")
                        .append(Component.literal(this.getLifespanMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
            if (this.getProductivityMod() != 1)
                components.add(Component.translatable("gene.complicated_bees.productivity_label")
                        .append(": ")
                        .append(Component.literal(this.getProductivityMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
            if (!this.getTemperatureMod().equals(EnumTolerance.NONE))
                components.add(Component.translatable("gene.complicated_bees.temperature_label")
                        .append(": ")
                        .append(this.getTemperatureMod().getTranslationKey())
                        .withStyle(ChatFormatting.GRAY));
            if (!this.getHumidityMod().equals(EnumTolerance.NONE))
                components.add(Component.translatable("gene.complicated_bees.humidity_label")
                        .append(": ")
                        .append(this.getHumidityMod().getTranslationKey())
                        .withStyle(ChatFormatting.GRAY));
            if (this.getTerritoryMod() != 1f)
                components.add(Component.translatable("gene.complicated_bees.territory_label")
                        .append(": ")
                        .append(Component.literal(this.getTerritoryMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
            if (this.getMutationMod() != 1f)
                components.add(Component.translatable("gui.complicated_bees.jei.mutations")
                        .append(": ")
                        .append(Component.literal(this.getMutationMod() + "x"))
                        .withStyle(ChatFormatting.GRAY));
            if (this.getSleepOverride())
                components.add(Component.translatable("frame.complicated_bees.overrides_sleep")
                        .withStyle(ChatFormatting.GRAY));
            if (this.getSkyOverride())
                components.add(Component.translatable("frame.complicated_bees.overrides_sky")
                        .withStyle(ChatFormatting.GRAY));
            if (this.getRainOverride())
                components.add(Component.translatable("frame.complicated_bees.overrides_rain")
                        .withStyle(ChatFormatting.GRAY));
        }

        return components;
    }

    public static class Builder {
        private EnumTolerance temperatureMod = EnumTolerance.NONE;
        private EnumTolerance humidityMod = EnumTolerance.NONE;
        private float lifespanMod = 1;
        private float productivityMod = 1;
        private float territoryMod = 1;
        private float mutationMod = 1;
        private boolean skyOverride = false;
        private boolean rainOverride = false;
        private boolean sleepOverride = false;

        public BeeHousingModifier build() {
            return new BeeHousingModifier(temperatureMod, humidityMod, lifespanMod, productivityMod, territoryMod, mutationMod, skyOverride, rainOverride, sleepOverride);
        }

        public Builder temperature(EnumTolerance mod) {
            this.temperatureMod = mod;
            return this;
        }

        public Builder humidity(EnumTolerance mod) {
            this.humidityMod = mod;
            return this;
        }

        public Builder lifespan(float mod) {
            this.lifespanMod = mod;
            return this;
        }

        public Builder productivity(float mod) {
            this.productivityMod = mod;
            return this;
        }

        public Builder territory(float mod) {
            this.territoryMod = mod;
            return this;
        }

        public Builder mutation(float mod) {
            this.mutationMod = mod;
            return this;
        }

        public Builder skyOverride(boolean value) {
            this.skyOverride = value;
            return this;
        }

        public Builder rainOverride(boolean value) {
            this.rainOverride = value;
            return this;
        }

        public Builder sleepOverride(boolean value) {
            this.sleepOverride = value;
            return this;
        }
    }
}
