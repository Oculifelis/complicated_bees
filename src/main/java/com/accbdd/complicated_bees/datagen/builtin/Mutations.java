package com.accbdd.complicated_bees.datagen.builtin;

import com.accbdd.complicated_bees.bees.Species;
import net.minecraft.resources.ResourceKey;

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
        mutation("noble/majestic", BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.MAJESTIC.getKey(), 0.10f);
        mutation("noble/imperial", BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.NOBLE.getKey(), 0.08f);
    }
}
