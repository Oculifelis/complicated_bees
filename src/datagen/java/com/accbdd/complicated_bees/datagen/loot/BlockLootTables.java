package com.accbdd.complicated_bees.datagen.loot;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumEnergyCellBlockEntity;
import com.accbdd.complicated_bees.loot.InheritHiveCombFunction;
import com.accbdd.complicated_bees.loot.InheritHiveSpeciesFunction;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;

public class BlockLootTables extends BlockLootSubProvider {
    public static final CompoundTag ENERGY_TAG_EMPTY = new CompoundTag();

    public BlockLootTables(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
        ENERGY_TAG_EMPTY.put(MellariumEnergyCellBlockEntity.ENERGY_TAG, IntTag.valueOf(0));
    }

    @Override
    protected void generate() {
        dropSelf(BlocksRegistration.APIARY.get());
        dropSelf(BlocksRegistration.CENTRIFUGE.get());
        dropSelf(BlocksRegistration.FURNACE_GENERATOR.get());
        dropSelf(BlocksRegistration.HONEY_GENERATOR.get());
        dropSelf(BlocksRegistration.APID_LIBRARY.get());
        dropSelf(BlocksRegistration.MICROSCOPE.get());
        dropSelf(BlocksRegistration.BEE_SORTER.get());
        dropSelf(BlocksRegistration.AUTOLYZER.get());
        dropSelf(BlocksRegistration.MELLARIUM_BASE.get());
        this.add(BlocksRegistration.MELLARIUM_CONTROLLER.get(), createSingleItemTable(ItemsRegistration.MELLARIUM_BASE.get()));
        dropSelf(BlocksRegistration.MELLARIUM_TEMP_UNIT.get());
        dropSelf(BlocksRegistration.MELLARIUM_FRAME_HOUSING_1.get());
        dropSelf(BlocksRegistration.MELLARIUM_FRAME_HOUSING_2.get());
        dropSelf(BlocksRegistration.MELLARIUM_FRAME_HOUSING_3.get());
        dropSelf(BlocksRegistration.MELLARIUM_RAIN_SHIELD.get());
        dropSelf(BlocksRegistration.MELLARIUM_MUTATOR.get());
        dropSelf(BlocksRegistration.MELLARIUM_HYDROREGULATOR.get());
        this.add(BlocksRegistration.MELLARIUM_ENERGY_CELL.get(), energyCellBlock(BlocksRegistration.MELLARIUM_ENERGY_CELL.get()));
        dropSelf(BlocksRegistration.MELLARIUM_SKYBOX.get());
        dropSelf(BlocksRegistration.MELLARIUM_TEMPORAL_SIMULATOR.get());
        dropSelf(BlocksRegistration.MELLARIUM_OUTPUT_HATCH.get());
        this.add(BlocksRegistration.GYROFUGE_ENERGY_CELL.get(), energyCellBlock(BlocksRegistration.GYROFUGE_ENERGY_CELL.get()));
        dropSelf(BlocksRegistration.GYROFUGE_BASE.get());
        this.add(BlocksRegistration.GYROFUGE_CONTROLLER.get(), createSingleItemTable(ItemsRegistration.GYROFUGE_BASE.get()));
        dropSelf(BlocksRegistration.GYROFUGE_BASIC_PROCESSING_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_PROCESSING_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_ADVANCED_PROCESSING_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_SPEED_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_EFFICIENCY_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_EXTRACTION_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_BASIC_SPEED_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_ADVANCED_SPEED_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_BASIC_EFFICIENCY_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_ADVANCED_EFFICIENCY_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_BASIC_EXTRACTION_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_ADVANCED_EXTRACTION_UNIT.get());
        dropSelf(BlocksRegistration.GYROFUGE_OUTPUT_HATCH.get());
        dropSelf(BlocksRegistration.GYROFUGE_INPUT_HATCH.get());
        this.add(BlocksRegistration.BEE_NEST.get(), nestLootTable(BlocksRegistration.BEE_NEST.get()));
        dropSelf(BlocksRegistration.WAX_BLOCK.get());
        dropSelf(BlocksRegistration.WAX_BLOCK_STAIRS.get());
        this.add(BlocksRegistration.WAX_BLOCK_SLAB.get(), createSlabItemTable(BlocksRegistration.WAX_BLOCK_SLAB.get()));
        dropSelf(BlocksRegistration.WAX_BLOCK_WALL.get());
        dropSelf(BlocksRegistration.SMOOTH_WAX.get());
        dropSelf(BlocksRegistration.SMOOTH_WAX_STAIRS.get());
        this.add(BlocksRegistration.SMOOTH_WAX_SLAB.get(), createSlabItemTable(BlocksRegistration.SMOOTH_WAX_SLAB.get()));
        dropSelf(BlocksRegistration.SMOOTH_WAX_WALL.get());
        dropSelf(BlocksRegistration.WAX_BRICKS.get());
        dropSelf(BlocksRegistration.WAX_BRICK_STAIRS.get());
        this.add(BlocksRegistration.WAX_BRICK_SLAB.get(), createSlabItemTable(BlocksRegistration.WAX_BRICK_SLAB.get()));
        dropSelf(BlocksRegistration.WAX_BRICK_WALL.get());
        dropSelf(BlocksRegistration.CHISELED_WAX.get());
        dropSelf(BlocksRegistration.HONEYED_PLANKS.get());
        dropSelf(BlocksRegistration.HONEYED_STAIRS.get());
        this.add(BlocksRegistration.HONEYED_SLAB.get(), createSlabItemTable(BlocksRegistration.HONEYED_SLAB.get()));
        dropSelf(BlocksRegistration.HONEYED_FENCE.get());
        dropSelf(BlocksRegistration.HONEYED_FENCE_GATE.get());
        dropSelf(BlocksRegistration.HONEYED_BUTTON.get());
        dropSelf(BlocksRegistration.HONEYED_PRESSURE_PLATE.get());
        this.add(BlocksRegistration.HONEYED_DOOR.get(), createDoorTable(BlocksRegistration.HONEYED_DOOR.get()));
        dropSelf(BlocksRegistration.HONEYED_TRAPDOOR.get());
        dropSelf(BlocksRegistration.HONEYED_SIGN.get());
        dropOther(BlocksRegistration.HONEYED_WALL_SIGN.get(), ItemsRegistration.HONEYED_SIGN.get());
        dropSelf(BlocksRegistration.HONEYED_HANGING_SIGN.get());
        dropOther(BlocksRegistration.HONEYED_WALL_HANGING_SIGN.get(), ItemsRegistration.HONEYED_HANGING_SIGN.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlocksRegistration.BLOCKS.getEntries()
                .stream()
                .map(DeferredHolder::get)
                .map(block -> (Block) block) // TODO???
                .toList();
    }

    public LootTable.Builder nestLootTable(Block beenest) {
        var fortune = registries.lookup(Registries.ENCHANTMENT).flatMap(lookup -> lookup.get(Enchantments.FORTUNE)).orElseThrow();
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(hasSilkTouch())
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(
                                LootItem.lootTableItem(beenest).apply(CopyCustomDataFunction
                                        .copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("species", "BlockEntityTag.species", CopyCustomDataFunction.MergeStrategy.REPLACE)
                                )
                        ))
                .withPool(LootPool.lootPool()
                        .when(doesNotHaveSilkTouch())
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(
                                LootItem.lootTableItem(ItemsRegistration.PRINCESS.get()).apply(InheritHiveSpeciesFunction.set())
                        ))
                .withPool(LootPool.lootPool()
                        .when(doesNotHaveSilkTouch())
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(
                                LootItem.lootTableItem(ItemsRegistration.DRONE.get())
                                        .apply(InheritHiveSpeciesFunction.set())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(fortune, 1))
                        ))
                .withPool(LootPool.lootPool()
                        .when(doesNotHaveSilkTouch())
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(
                                LootItem.lootTableItem(ItemsRegistration.COMB.get())
                                        .apply(InheritHiveCombFunction.set())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(fortune, 1))
                        )
                );
    }

    protected LootTable.Builder energyCellBlock(Block pBlock) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(
                                LootItem.lootTableItem(pBlock)
                                        .apply(CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("energy", "BlockEntityTag.energy"))
                        )
                );
    }
}
