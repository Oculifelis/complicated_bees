package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTemperature;
import com.accbdd.complicated_bees.bees.mutation.condition.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Map;

import static com.accbdd.complicated_bees.datagen.builtin.BuiltIn.mutation;

public class Mutations {
    private static final List<Map.Entry<ResourceKey<Species>, Species>> COMMON_MUTATORS = List.of(BuiltInSpecies.FOREST, BuiltInSpecies.PLAINS, BuiltInSpecies.JUNGLE, BuiltInSpecies.DESERT, BuiltInSpecies.ROCKY);
    private static final List<Map.Entry<ResourceKey<Species>, Species>> CULTIVATED_MUTATORS = List.of(BuiltInSpecies.FOREST, BuiltInSpecies.PLAINS);
    public static void generateMutations() {
        for (int i = 0; i < COMMON_MUTATORS.size()-1; i++) {
            for (int j = i+1; j < COMMON_MUTATORS.size(); j++) {
                var first = COMMON_MUTATORS.get(i);
                var second = COMMON_MUTATORS.get(j);
                mutation("apis/" + first.getKey().location().getPath() + "_" + second.getKey().location().getPath() + "_common", first.getKey(), second.getKey(), BuiltInSpecies.COMMON.getKey(), 0.15f);
            }
        }

        for (Map.Entry<ResourceKey<Species>, Species> entry : CULTIVATED_MUTATORS) {
            mutation("apis/" + entry.getKey().location().getPath() + "_cultivated", entry.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), 0.12f);
        }

        mutation("noble/noble", BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.NOBLE.getKey(), 0.10f);
        mutation("noble/majestic", BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.MAJESTIC.getKey(), 0.10f);
        mutation("noble/imperial", BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.IMPERIAL.getKey(), 0.08f);

        mutation("desert/outcast", BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.OUTCAST.getKey(), 0.10f);
        mutation("desert/bandit", BuiltInSpecies.OUTCAST.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.BANDIT.getKey(), 0.08f);

        mutation("jungle/tangle", BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.TANGLE.getKey(), 0.10f);
        mutation("jungle/lush", BuiltInSpecies.TANGLE.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.LUSH.getKey(), 0.08f);

        mutation("ender/surreal", BuiltInSpecies.ENDER.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.SURREAL.getKey(), 0.10f, new DimensionCondition(ResourceLocation.tryParse("minecraft:the_end")));
        mutation("ender/enigmatic", BuiltInSpecies.SURREAL.getKey(), BuiltInSpecies.INTREPID.getKey(), BuiltInSpecies.ENIGMATIC.getKey(), 0.10f, new DimensionCondition(ResourceLocation.tryParse("minecraft:the_end")));

        mutation("heroic/explorer", BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.EXPLORER.getKey(), 0.12f);
        mutation("heroic/intrepid", BuiltInSpecies.EXPLORER.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.INTREPID.getKey(), 0.10f);
        mutation("heroic/champion", BuiltInSpecies.INTREPID.getKey(), BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.CHAMPION.getKey(), 0.08f);

        mutation("industrious/diligent", BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.DILIGENT.getKey(), 0.12f);
        mutation("industrious/tireless", BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.TIRELESS.getKey(), 0.10f);
        mutation("industrious/industrious", BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.INDUSTRIOUS.getKey(), 0.08f);

        mutation("infernal/cursed", BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.CURSED.getKey(), 0.12f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutation("infernal/fiendish", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.FIENDISH.getKey(), 0.10f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutation("infernal/devilish", BuiltInSpecies.FIENDISH.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.DEVILISH.getKey(), 0.08f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutation("infernal/infernal", BuiltInSpecies.DEVILISH.getKey(), BuiltInSpecies.BANDIT.getKey(), BuiltInSpecies.INFERNAL.getKey(), 0.08f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutation("infernal/haunted", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.HAUNTED.getKey(), 0.12f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH), new BlockUnderCondition(Blocks.SOUL_SAND));
        mutation("infernal/ghostly", BuiltInSpecies.HAUNTED.getKey(), BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.GHOSTLY.getKey(), 0.10f);
        mutation("infernal/spectral", BuiltInSpecies.GHOSTLY.getKey(), BuiltInSpecies.DEVILISH.getKey(), BuiltInSpecies.SPECTRAL.getKey(), 0.08f);

        mutation("metallic/cuprous", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.CUPROUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.WAXED_COPPER_BLOCK));
        mutation("metallic/precious", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.PRECIOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.GOLD_BLOCK));
        mutation("metallic/ferrous", BuiltInSpecies.CUPROUS.getKey(), BuiltInSpecies.PRECIOUS.getKey(), BuiltInSpecies.FERROUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.IRON_BLOCK));
        mutation("metallic/adamantine", BuiltInSpecies.FERROUS.getKey(), BuiltInSpecies.LUMINOUS.getKey(), BuiltInSpecies.ADAMANTINE.getKey(), 0.06f, new BlockUnderCondition(Blocks.NETHERITE_BLOCK));

        mutation("mineral/bituminous", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.BITUMINOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.COAL_BLOCK));
        mutation("mineral/conductive", BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.CONDUCTIVE.getKey(), 0.10f, new BlockUnderCondition(Blocks.REDSTONE_BLOCK));
        mutation("mineral/lapic", BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.LAPIC.getKey(), 0.10f, new BlockUnderCondition(Blocks.LAPIS_BLOCK));
        mutation("mineral/amethyst", BuiltInSpecies.CONDUCTIVE.getKey(), BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.AMETHYST.getKey(), 0.10f, new BlockUnderCondition(Blocks.AMETHYST_BLOCK));
        mutation("mineral/dimantic", BuiltInSpecies.LAPIC.getKey(), BuiltInSpecies.AMETHYST.getKey(), BuiltInSpecies.DIMANTIC.getKey(), 0.06f, new BlockUnderCondition(Blocks.DIAMOND_BLOCK));
        mutation("mineral/emeradic", BuiltInSpecies.AMETHYST.getKey(), BuiltInSpecies.CONDUCTIVE.getKey(), BuiltInSpecies.EMERADIC.getKey(), 0.08f, new BlockUnderCondition(Blocks.EMERALD_BLOCK));
        mutation("mineral/quartz", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.QUARTZ.getKey(), 0.10f, new BlockUnderCondition(Blocks.QUARTZ_BLOCK), new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutation("mineral/luminous", BuiltInSpecies.QUARTZ.getKey(), BuiltInSpecies.HAUNTED.getKey(), BuiltInSpecies.LUMINOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.GLOWSTONE), new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));

        mutation("necrotic/decaying", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.OUTCAST.getKey(), BuiltInSpecies.DECAYING.getKey(), 0.10f);
        mutation("necrotic/rotten", BuiltInSpecies.DECAYING.getKey(), BuiltInSpecies.TANGLE.getKey(), BuiltInSpecies.ROTTEN.getKey(), 0.10f);
        mutation("necrotic/necromantic", BuiltInSpecies.ROTTEN.getKey(), BuiltInSpecies.LUSH.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), 0.08f, new NighttimeCondition(), new TemperatureCondition(EnumTemperature.FROZEN, EnumTemperature.COLD));

        mutation("rocky/robust", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.ROBUST.getKey(), 0.12f);
        mutation("rocky/resilient", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.RESILIENT.getKey(), 0.08f);

        mutation("creative/jazzy", BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.JAZZY.getKey(), 0.10f);
        mutation("creative/essayist", BuiltInSpecies.JAZZY.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.ESSAYIST.getKey(), 0.10f);
        mutation("creative/tricky", BuiltInSpecies.FIENDISH.getKey(), BuiltInSpecies.ROTTEN.getKey(), BuiltInSpecies.TRICKY.getKey(), 0.10f);

        mutation("terraforming/primordial", BuiltInSpecies.ENIGMATIC.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), BuiltInSpecies.PRIMORDIAL.getKey(), 0.08f, new BiomeCondition(BiomeTags.IS_END));
        mutation("terraforming/campestral", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.PLAINS.getKey(), BuiltInSpecies.CAMPESTRAL.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_PLAINS));
        mutation("terraforming/sylvan", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.FOREST.getKey(), BuiltInSpecies.SYLVAN.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_FOREST));
        mutation("terraforming/boreal", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.BOREAL.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_TAIGA));
        mutation("terraforming/tropic", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.TROPIC.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_JUNGLE));
        mutation("terraforming/paludal", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.DECAYING.getKey(), BuiltInSpecies.PALUDAL.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_SWAMP));
        mutation("terraforming/gelid", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), BuiltInSpecies.GELID.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_SNOWY));
        mutation("terraforming/mycelic", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.MYCELIC.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_MUSHROOM));
        mutation("terraforming/xeric", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.XERIC.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_DESERT));
    }
}
