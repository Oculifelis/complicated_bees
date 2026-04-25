package com.accbdd.complicated_bees.util.enums;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.gene.GeneActiveTime;
import com.accbdd.complicated_bees.bees.gene.GeneFlower;
import com.accbdd.complicated_bees.bees.gene.GeneHumidity;
import com.accbdd.complicated_bees.bees.gene.GeneTemperature;
import com.accbdd.complicated_bees.bees.gene.enums.EnumActiveTime;
import com.accbdd.complicated_bees.bees.gene.enums.EnumHumidity;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTemperature;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public enum EnumErrorCodes {
    NO_FLOWER("no_flower", 1, EnumErrorCodes::flowerComponent),
    WRONG_TEMP("wrong_temp", 2, EnumErrorCodes::tempComponent),
    WRONG_HUMIDITY("wrong_humidity", 4, EnumErrorCodes::humidComponent),
    OUTPUT_FULL("output_full", 8, (stack, cond) -> defaultGetter("output_full")),
    WRONG_TIME("wrong_time", 16, EnumErrorCodes::timeComponent),
    UNDERGROUND("underground", 32, (stack, cond) -> defaultGetter("underground")),
    WEATHER("weather", 64, (stack, cond) -> defaultGetter("weather")),
    ECSTATIC("ecstatic", 128, (stack, cond) -> defaultGetter("ecstatic")),
    NOT_UNDERGROUND("not_underground", 256, (stack, cond) -> defaultGetter("not_underground"));

    public final String name;
    public final int value;
    public final BiFunction<ItemStack, Conditions, Component> detailGetter;

    /**
     * @param name the name of the error, used for localization
     * @param value the numeric value of the error code, used as flag id
     * @param detailGetter a function that returns the specific condition the bee needs
     */
    EnumErrorCodes(String name, int value, BiFunction<ItemStack, Conditions, Component> detailGetter) {
        this.name = name;
        this.value = value;
        this.detailGetter = detailGetter;
    }

    public static MutableComponent flowerComponent(ItemStack stack, Conditions cond) {
        return Component.translatable("gui.complicated_bees.error.no_flower", Component.translatable("flower.complicated_bees." + GeneticHelper.getGeneValue(stack, GeneFlower.ID, true)));
    }

    public static MutableComponent tempComponent(ItemStack stack, Conditions cond) {
        return Component.translatable("gui.complicated_bees.error.wrong_temp", cond.temp().getTranslationKey(), ((EnumTemperature)GeneticHelper.getGeneValue(stack, GeneTemperature.ID, true)).getTranslationKey());
    }

    public static MutableComponent humidComponent(ItemStack stack, Conditions cond) {
        return Component.translatable("gui.complicated_bees.error.wrong_humidity", cond.humidity().getTranslationKey(), ((EnumHumidity)GeneticHelper.getGeneValue(stack, GeneHumidity.ID, true)).getTranslationKey());
    }

    public static MutableComponent timeComponent(ItemStack stack, Conditions cond) {
        return Component.translatable("gui.complicated_bees.error.wrong_time", ((EnumActiveTime)GeneticHelper.getGeneValue(stack, GeneActiveTime.ID, true)).getTranslationKey());
    }

    public static MutableComponent defaultGetter(String name) {
        return Component.translatable("gui.complicated_bees.error." + name);
    }

    public record Conditions(EnumTemperature temp, EnumHumidity humidity) {

    }
}
