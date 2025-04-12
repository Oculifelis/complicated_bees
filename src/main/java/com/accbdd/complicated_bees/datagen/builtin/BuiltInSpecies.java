package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.*;
import com.accbdd.complicated_bees.bees.gene.enums.*;
import com.accbdd.complicated_bees.registry.BeeEffectRegistration;
import com.accbdd.complicated_bees.registry.GeneRegistration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

import static com.accbdd.complicated_bees.datagen.builtin.BuiltIn.*;

public class BuiltInSpecies {
    public static final List<ResourceLocation> RED_MODELS = List.of(loc("item/red_drone"), loc("item/red_princess"), loc("item/red_queen"));
    public static final List<ResourceLocation> GRAY_MODELS = List.of(loc("item/gray_drone"), loc("item/gray_princess"), loc("item/gray_queen"));

    public static final Map.Entry<ResourceKey<Species>, Species> ADAMANTINE = species(new Species.Builder(loc("adamantine"))
            .dominant(false)
            .foil(true)
            .models(RED_MODELS)
            .products(List.of(new Product(stack(Combs.SIMMERING), 0.3f), new Product(Items.NETHERITE_INGOT.getDefaultInstance(), 0.05f)))
            .colors(0x4d453d)
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.AVERAGE, false))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.HOT, EnumTolerance.UP_1, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOWER, false))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(1, false))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.DEBRIS.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.NEVER_SLEEPS, false))
            .gene(GeneRegistration.CAVE_DWELLING, new GeneBoolean(true, true))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(true, true))
    );

    public static final Map.Entry<ResourceKey<Species>, Species> FOREST = species(new Species.Builder(loc("forest"))
            .dominant(true)
            .foil(false)
            .colors(0x3fce8)
            .products(List.of(new Product(stack(Combs.HONEY), 0.35f)))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORTEST, true))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.DOWN_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOWEST, true))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(2, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.FLOWER.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.DIURNAL, true))
            .gene(GeneRegistration.CAVE_DWELLING, new GeneBoolean(false, true))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(false, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> PLAINS = species(Species.Builder.of(FOREST.getValue(), loc("plains"))
            .colors(0xfc030f, 0xc92d02)
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORTER, true))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.UP_1, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> JUNGLE = species(Species.Builder.of(FOREST.getValue(), loc("jungle"))
            .colors(0x7bbf04, 0x4f721e)
            .products(List.of(new Product(stack(Combs.SILKY), 0.35f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.WARM, EnumTolerance.UP_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.WET, EnumTolerance.NONE, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOWER, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.JUNGLE.getKey().location(), true))
            .gene(GeneRegistration.EFFECT, new GeneEffect(BeeEffectRegistration.VENOMOUS.get(), true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> DESERT = species(Species.Builder.of(FOREST.getValue(), loc("desert"))
            .colors(0xffdf87)
            .products(List.of(new Product(stack(Combs.DUSTY), 0.35f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.HOT, EnumTolerance.DOWN_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.DRY, EnumTolerance.NONE, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.DESERT.getKey().location(), true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> ROCKY = species(Species.Builder.of(FOREST.getValue(), loc("rocky"))
            .colors(0x7e7e7e, 0x9d416b)
            .products(List.of(new Product(stack(Combs.ROCKY), 0.35f)))
            .models(GRAY_MODELS)
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.LUSH_CAVE.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.NOCTURNAL, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> COMMON = species(Species.Builder.of(PLAINS.getValue(), loc("common"))
            .colors(0xc2c2c2)
            .products(List.of(new Product(stack(Combs.HONEY), 0.4f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOWER, true))
    );

    public static final Map.Entry<ResourceKey<Species>, Species> CULTIVATED = species(Species.Builder.of(COMMON.getValue(), loc("cultivated"))
            .colors(0x4542f5)
            .products(List.of(new Product(stack(Combs.HONEY), 0.5f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.AVERAGE, true))
    );
}
