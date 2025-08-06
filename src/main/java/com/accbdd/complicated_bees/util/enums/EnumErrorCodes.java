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

import java.util.function.Function;

public enum EnumErrorCodes {
    NO_FLOWER("no_flower", 1, EnumErrorCodes::flowerComponentFromStack),
    WRONG_TEMP("wrong_temp", 2, EnumErrorCodes::tempComponentFromStack),
    WRONG_HUMIDITY("wrong_humidity", 4, EnumErrorCodes::humidComponentFromStack),
    OUTPUT_FULL("output_full", 8, stack -> Component.empty()),
    WRONG_TIME("wrong_time", 16, EnumErrorCodes::timeComponentFromStack),
    UNDERGROUND("underground", 32, stack -> Component.empty()),
    WEATHER("weather", 64, stack -> Component.empty()),
    ECSTATIC("ecstatic", 128, stack -> Component.empty()),
    NOT_UNDERGROUND("not_underground", 256, stack -> Component.empty());

    public final String name;
    public final int value;
    public final Function<ItemStack, Component> detailGetter;

    /**
     * @param name the name of the error, used for localization
     * @param value the numeric value of the error code, used as flag id
     * @param detailGetter a function that returns the specific condition the bee needs
     */
    EnumErrorCodes(String name, int value, Function<ItemStack, Component> detailGetter) {
        this.name = name;
        this.value = value;
        this.detailGetter = detailGetter;
    }

    public static MutableComponent flowerComponentFromStack(ItemStack stack) {
        return Component.translatable("flower.complicated_bees." + GeneticHelper.getGeneValue(stack, GeneFlower.ID, true));
    }

    public static MutableComponent tempComponentFromStack(ItemStack stack) {
        return ((EnumTemperature)GeneticHelper.getGeneValue(stack, GeneTemperature.ID, true)).getTranslationKey();
    }

    public static MutableComponent humidComponentFromStack(ItemStack stack) {
        return ((EnumHumidity)GeneticHelper.getGeneValue(stack, GeneHumidity.ID, true)).getTranslationKey();
    }

    public static MutableComponent timeComponentFromStack(ItemStack stack) {
        return ((EnumActiveTime)GeneticHelper.getGeneValue(stack, GeneActiveTime.ID, true)).getTranslationKey();
    }
}
