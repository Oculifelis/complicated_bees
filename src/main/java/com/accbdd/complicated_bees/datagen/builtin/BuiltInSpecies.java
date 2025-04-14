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
            .colors(0x03fce8)
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
    public static final Map.Entry<ResourceKey<Species>, Species> TANGLE = species(Species.Builder.of(JUNGLE.getValue(), loc("tangle"))
            .colors(0x649d01)
            .products(List.of(new Product(stack(Combs.SILKY), 0.4f)))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORTER, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOW, true))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(true, false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> LUSH = species(Species.Builder.of(TANGLE.getValue(), loc("lush"))
            .colors(0xa3d945)
            .products(List.of(new Product(stack(Combs.SILKY), 0.45f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.WARM, EnumTolerance.BOTH_1, false))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(true, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> DESERT = species(Species.Builder.of(FOREST.getValue(), loc("desert"))
            .colors(0xffdf87)
            .products(List.of(new Product(stack(Combs.DUSTY), 0.35f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.HOT, EnumTolerance.DOWN_1, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.DRY, EnumTolerance.NONE, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.DESERT.getKey().location(), true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> OUTCAST = species(Species.Builder.of(DESERT.getValue(), loc("outcast"))
            .colors(0xfcda72)
            .products(List.of(new Product(stack(Combs.DUSTY), 0.4f)))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORT, true))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOW, false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> BANDIT = species(Species.Builder.of(OUTCAST.getValue(), loc("bandit"))
            .colors(0xcd6a18)
            .products(List.of(new Product(stack(Combs.DUSTY), 0.4f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.HOT, EnumTolerance.DOWN_2, true))
            .gene(GeneRegistration.EFFECT, new GeneEffect(BeeEffectRegistration.TRIBUTE.get(), true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> ROCKY = species(Species.Builder.of(FOREST.getValue(), loc("rocky"))
            .colors(0x7e7e7e, 0x9d416b)
            .products(List.of(new Product(stack(Combs.ROCKY), 0.35f)))
            .models(GRAY_MODELS)
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.LUSH_CAVE.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.NOCTURNAL, true))
            .gene(GeneRegistration.CAVE_DWELLING, new GeneBoolean(true, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> ROBUST = species(Species.Builder.of(ROCKY.getValue(), loc("robust"))
            .colors(0x999999)
            .products(List.of(new Product(stack(Combs.ROCKY), 0.35f)))
            .models(GRAY_MODELS)
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_1, false))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.UP_1, false))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOWER, true))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.AVERAGE, false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> RESILIENT = species(Species.Builder.of(ROCKY.getValue(), loc("resilient"))
            .colors(0xb8b7b7)
            .products(List.of(new Product(stack(Combs.ROCKY), 0.4f)))
            .models(GRAY_MODELS)
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_1, false))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.UP_1, false))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.LONG, false))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(1, false))
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
            .products(List.of(new Product(stack(Combs.HONEY), 0.4f)))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOW, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> NOBLE = species(new Species.Builder(loc("noble"))
            .dominant(false)
            .foil(false)
            .colors(0xffca38)
            .products(List.of(new Product(stack(Combs.DRIPPING), 0.4f)))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORT, false))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.NONE, false))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, false))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOW, false))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(2, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.FLOWER.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.DIURNAL, true))
            .gene(GeneRegistration.CAVE_DWELLING, new GeneBoolean(false, true))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(false, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> MAJESTIC = species(Species.Builder.of(NOBLE.getValue(), loc("majestic"))
            .colors(0x840121)
            .specialtyProducts(List.of(new Product(stack(Combs.ROYAL), 0.25f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.WARM, EnumTolerance.DOWN_1, true))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(3, false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> IMPERIAL = species(Species.Builder.of(MAJESTIC.getValue(), loc("imperial"))
            .colors(0x840121)
            .foil(true)
            .products(List.of(new Product(stack(Combs.DRIPPING), 0.5f)))
            .specialtyProducts(List.of(new Product(stack(Combs.ROYAL), 0.4f)))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.WARM, EnumTolerance.DOWN_1, true))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(4, false))
            .gene(GeneRegistration.EFFECT, new GeneEffect(BeeEffectRegistration.BEATIFIC.get(), false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> DILIGENT = species(new Species.Builder(loc("diligent"))
            .colors(0xb642f5)
            .products(List.of(new Product(stack(Combs.STRINGY), 0.35f), new Product(stack(Combs.HONEY), 0.1f)))
            .gene(GeneRegistration.LIFESPAN, new GeneLifespan(EnumLifespan.SHORT, false))
            .gene(GeneRegistration.TEMPERATURE, new GeneTemperature(EnumTemperature.NORMAL, EnumTolerance.BOTH_1, false))
            .gene(GeneRegistration.HUMIDITY, new GeneHumidity(EnumHumidity.NORMAL, EnumTolerance.NONE, false))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.SLOW, false))
            .gene(GeneRegistration.FERTILITY, new GeneFertility(2, true))
            .gene(GeneRegistration.FLOWER, new GeneFlower(Flowers.FLOWER.getKey().location(), true))
            .gene(GeneRegistration.ACTIVE_TIME, new GeneActiveTime(EnumActiveTime.DIURNAL, true))
            .gene(GeneRegistration.CAVE_DWELLING, new GeneBoolean(false, true))
            .gene(GeneRegistration.WEATHERPROOF, new GeneBoolean(false, true))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> TIRELESS = species(Species.Builder.of(DILIGENT.getValue(), loc("tireless"))
            .colors(0x4dfe88)
            .products(List.of(new Product(stack(Combs.STRINGY), 0.4f), new Product(stack(Combs.HONEY), 0.1f)))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.AVERAGE, false))
    );
    public static final Map.Entry<ResourceKey<Species>, Species> INDUSTRIOUS = species(Species.Builder.of(DILIGENT.getValue(), loc("industrious"))
            .colors(0xecfafe)
            .foil(true)
            .products(List.of(new Product(stack(Combs.STRINGY), 0.4f), new Product(stack(Combs.HONEY), 0.1f)))
            .gene(GeneRegistration.PRODUCTIVITY, new GeneProductivity(EnumProductivity.AVERAGE, true))
    );
}
