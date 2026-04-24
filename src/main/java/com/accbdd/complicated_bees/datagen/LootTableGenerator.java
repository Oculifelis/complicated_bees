package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.datagen.loot.BlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class LootTableGenerator extends LootTableProvider {

    private static final List<LootTableProvider.SubProviderEntry> entries = List.of(
            new LootTableProvider.SubProviderEntry(
                    BlockLootTables::new,
                    LootContextParamSets.BLOCK
            )
    );

    public LootTableGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(pOutput, Collections.emptySet(), entries, registries);
    }
}
