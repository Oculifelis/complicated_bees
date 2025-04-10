package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.datagen.advancement.SubAdvancementGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BeeAdvancementGenerator extends ForgeAdvancementProvider {
    private static final List<AdvancementGenerator> entries = List.of(
            new SubAdvancementGenerator()
    );

    public BeeAdvancementGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, entries);
    }
}
