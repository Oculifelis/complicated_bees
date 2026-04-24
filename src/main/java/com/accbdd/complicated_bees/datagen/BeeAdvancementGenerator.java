package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.datagen.advancement.SubAdvancementGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BeeAdvancementGenerator extends AdvancementProvider {
    private static final List<AdvancementGenerator> entries = List.of(
            new SubAdvancementGenerator()
    );

    public BeeAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, entries);
    }
}
