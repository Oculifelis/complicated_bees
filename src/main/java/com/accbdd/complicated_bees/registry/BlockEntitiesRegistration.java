package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.block.entity.*;
import com.accbdd.complicated_bees.block.entity.gyrofuge.*;
import com.accbdd.complicated_bees.block.entity.mellarium.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BlockEntitiesRegistration {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final Supplier<BlockEntityType<SignBlockEntityCB>> CB_SIGN_ENTITY = BLOCK_ENTITIES.register("sign_cb",
            () -> new BlockEntityType<>(SignBlockEntityCB::new, BlocksRegistration.HONEYED_SIGN.get(), BlocksRegistration.HONEYED_WALL_SIGN.get()));
    public static final Supplier<BlockEntityType<HangingSignBlockEntityCB>> CB_HANGING_SIGN_ENTITY = BLOCK_ENTITIES.register("hanging_sign_cb",
            () -> new BlockEntityType<>(HangingSignBlockEntityCB::new, BlocksRegistration.HONEYED_HANGING_SIGN.get(), BlocksRegistration.HONEYED_WALL_HANGING_SIGN.get()));

    public static final Supplier<BlockEntityType<ApiaryBlockEntity>> APIARY_ENTITY = BLOCK_ENTITIES.register("apiary",
            () -> new BlockEntityType<>(ApiaryBlockEntity::new, BlocksRegistration.APIARY.get()));
    public static final Supplier<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE_ENTITY = BLOCK_ENTITIES.register("centrifuge",
            () -> new BlockEntityType<>(CentrifugeBlockEntity::new, BlocksRegistration.CENTRIFUGE.get()));
    public static final Supplier<BlockEntityType<BeeNestBlockEntity>> BEE_NEST_ENTITY = BLOCK_ENTITIES.register("bee_nest",
            () -> new BlockEntityType<>(BeeNestBlockEntity::new, BlocksRegistration.BEE_NEST.get()));
    public static final Supplier<BlockEntityType<FurnaceGeneratorBlockEntity>> FURNACE_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("furnace_generator",
            () -> new BlockEntityType<>(FurnaceGeneratorBlockEntity::new, BlocksRegistration.FURNACE_GENERATOR.get()));
    public static final Supplier<BlockEntityType<HoneyGeneratorBlockEntity>> HONEY_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("honey_generator",
            () -> new BlockEntityType<>(HoneyGeneratorBlockEntity::new, BlocksRegistration.HONEY_GENERATOR.get()));
    public static final Supplier<BlockEntityType<MicroscopeBlockEntity>> MICROSCOPE_BLOCK_ENTITY = BLOCK_ENTITIES.register("microscope",
            () -> new BlockEntityType<>(MicroscopeBlockEntity::new, BlocksRegistration.MICROSCOPE.get()));
    public static final Supplier<BlockEntityType<AutolyzerBlockEntity>> AUTOLYZER_BLOCK_ENTITY = BLOCK_ENTITIES.register("autolyzer",
            () -> new BlockEntityType<>(AutolyzerBlockEntity::new, BlocksRegistration.AUTOLYZER.get()));
    public static final Supplier<BlockEntityType<MellariumControllerBlockEntity>> MELLARIUM_CONTROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_controller",
            () -> new BlockEntityType<>(MellariumControllerBlockEntity::new, BlocksRegistration.MELLARIUM_CONTROLLER.get()));
    public static final Supplier<BlockEntityType<MellariumBaseBlockEntity>> MELLARIUM_BASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_base",
            () -> new BlockEntityType<>(MellariumBaseBlockEntity::new, BlocksRegistration.MELLARIUM_BASE.get()));
    public static final Supplier<BlockEntityType<MellariumTempUnitBlockEntity>> MELLARIUM_TEMP_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_temp_unit",
            () -> new BlockEntityType<>(MellariumTempUnitBlockEntity::new, BlocksRegistration.MELLARIUM_TEMP_UNIT.get()));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_1_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_1",
            () -> new BlockEntityType<>((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 1), BlocksRegistration.MELLARIUM_FRAME_HOUSING_1.get()));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_2_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_2",
            () -> new BlockEntityType<>((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 2), BlocksRegistration.MELLARIUM_FRAME_HOUSING_2.get()));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_3_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_3",
            () -> new BlockEntityType<>((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 3), BlocksRegistration.MELLARIUM_FRAME_HOUSING_3.get()));
    public static final Supplier<BlockEntityType<MellariumRainShieldBlockEntity>> MELLARIUM_RAIN_SHIELD_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_rain_shield",
            () -> new BlockEntityType<>(MellariumRainShieldBlockEntity::new, BlocksRegistration.MELLARIUM_RAIN_SHIELD.get()));
    public static final Supplier<BlockEntityType<MellariumMutatorBlockEntity>> MELLARIUM_MUTATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_mutator",
            () -> new BlockEntityType<>(MellariumMutatorBlockEntity::new, BlocksRegistration.MELLARIUM_MUTATOR.get()));
    public static final Supplier<BlockEntityType<MellariumHydroregulatorBlockEntity>> MELLARIUM_HYDROREGULATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_hydroregulator",
            () -> new BlockEntityType<>(MellariumHydroregulatorBlockEntity::new, BlocksRegistration.MELLARIUM_HYDROREGULATOR.get()));
    public static final Supplier<BlockEntityType<MellariumEnergyCellBlockEntity>> MELLARIUM_ENERGY_CELL_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_energy_cell",
            () -> new BlockEntityType<>(MellariumEnergyCellBlockEntity::new, BlocksRegistration.MELLARIUM_ENERGY_CELL.get()));
    public static final Supplier<BlockEntityType<MellariumSkyboxBlockEntity>> MELLARIUM_SKYBOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_skybox",
            () -> new BlockEntityType<>(MellariumSkyboxBlockEntity::new, BlocksRegistration.MELLARIUM_SKYBOX.get()));
    public static final Supplier<BlockEntityType<MellariumTemporalSimulatorBlockEntity>> MELLARIUM_TEMPORAL_SIMULATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_temporal_simulator",
            () -> new BlockEntityType<>(MellariumTemporalSimulatorBlockEntity::new, BlocksRegistration.MELLARIUM_TEMPORAL_SIMULATOR.get()));
    public static final Supplier<BlockEntityType<MellariumOutputHatchBlockEntity>> MELLARIUM_OUTPUT_HATCH_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_output_hatch",
            () -> new BlockEntityType<>(MellariumOutputHatchBlockEntity::new, BlocksRegistration.MELLARIUM_OUTPUT_HATCH.get()));
    public static final Supplier<BlockEntityType<BeeSorterBlockEntity>> BEE_SORTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("bee_sorter",
            () -> new BlockEntityType<>(BeeSorterBlockEntity::new, BlocksRegistration.BEE_SORTER.get()));
    public static final Supplier<BlockEntityType<GyrofugeControllerBlockEntity>> GYROFUGE_CONTROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_controller",
            () -> new BlockEntityType<>(GyrofugeControllerBlockEntity::new, BlocksRegistration.GYROFUGE_CONTROLLER.get()));
    public static final Supplier<BlockEntityType<GyrofugeBaseBlockEntity>> GYROFUGE_BASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_base",
            () -> new BlockEntityType<>(GyrofugeBaseBlockEntity::new, BlocksRegistration.GYROFUGE_BASE.get()));
    public static final Supplier<BlockEntityType<GyrofugeEnergyCellBlockEntity>> GYROFUGE_ENERGY_CELL_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_energy_cell",
            () -> new BlockEntityType<>(GyrofugeEnergyCellBlockEntity::new, BlocksRegistration.GYROFUGE_ENERGY_CELL.get()));
    public static final Supplier<BlockEntityType<GyrofugeBasicProcessingUnitBlockEntity>> GYROFUGE_BASIC_PROCESSING_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_basic_processing_unit",
            () -> new BlockEntityType<>(GyrofugeBasicProcessingUnitBlockEntity::new, BlocksRegistration.GYROFUGE_BASIC_PROCESSING_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeProcessingUnitBlockEntity>> GYROFUGE_PROCESSING_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_processing_unit",
            () -> new BlockEntityType<>(GyrofugeProcessingUnitBlockEntity::new, BlocksRegistration.GYROFUGE_PROCESSING_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeAdvancedProcessingUnitBlockEntity>> GYROFUGE_ADVANCED_PROCESSING_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_advanced_processing_unit",
            () -> new BlockEntityType<>(GyrofugeAdvancedProcessingUnitBlockEntity::new, BlocksRegistration.GYROFUGE_ADVANCED_PROCESSING_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeBasicSpeedUnitBlockEntity>> GYROFUGE_BASIC_SPEED_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_basic_speed_unit",
            () -> new BlockEntityType<>(GyrofugeBasicSpeedUnitBlockEntity::new, BlocksRegistration.GYROFUGE_BASIC_SPEED_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeSpeedUnitBlockEntity>> GYROFUGE_SPEED_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge__speed_unit",
            () -> new BlockEntityType<>(GyrofugeSpeedUnitBlockEntity::new, BlocksRegistration.GYROFUGE_SPEED_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeAdvancedSpeedUnitBlockEntity>> GYROFUGE_ADVANCED_SPEED_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_advanced_speed_unit",
            () -> new BlockEntityType<>(GyrofugeAdvancedSpeedUnitBlockEntity::new, BlocksRegistration.GYROFUGE_ADVANCED_SPEED_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeBasicEfficiencyUnitBlockEntity>> GYROFUGE_BASIC_EFFICIENCY_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_basic_efficiency_unit",
            () -> new BlockEntityType<>(GyrofugeBasicEfficiencyUnitBlockEntity::new, BlocksRegistration.GYROFUGE_BASIC_EFFICIENCY_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeEfficiencyUnitBlockEntity>> GYROFUGE_EFFICIENCY_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_efficiency_unit",
            () -> new BlockEntityType<>(GyrofugeEfficiencyUnitBlockEntity::new, BlocksRegistration.GYROFUGE_EFFICIENCY_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeAdvancedEfficiencyUnitBlockEntity>> GYROFUGE_ADVANCED_EFFICIENCY_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_advanced_efficiency_unit",
            () -> new BlockEntityType<>(GyrofugeAdvancedEfficiencyUnitBlockEntity::new, BlocksRegistration.GYROFUGE_ADVANCED_EFFICIENCY_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeBasicExtractionUnitBlockEntity>> GYROFUGE_BASIC_EXTRACTION_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_basic_extraction_unit",
            () -> new BlockEntityType<>(GyrofugeBasicExtractionUnitBlockEntity::new, BlocksRegistration.GYROFUGE_BASIC_EXTRACTION_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeExtractionUnitBlockEntity>> GYROFUGE_EXTRACTION_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_extraction_unit",
            () -> new BlockEntityType<>(GyrofugeExtractionUnitBlockEntity::new, BlocksRegistration.GYROFUGE_EXTRACTION_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeAdvancedExtractionUnitBlockEntity>> GYROFUGE_ADVANCED_EXTRACTION_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_advanced_extraction_unit",
            () -> new BlockEntityType<>(GyrofugeAdvancedExtractionUnitBlockEntity::new, BlocksRegistration.GYROFUGE_ADVANCED_EXTRACTION_UNIT.get()));
    public static final Supplier<BlockEntityType<GyrofugeOutputHatchBlockEntity>> GYROFUGE_OUTPUT_HATCH_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_output_hatch",
            () -> new BlockEntityType<>(GyrofugeOutputHatchBlockEntity::new, BlocksRegistration.GYROFUGE_OUTPUT_HATCH.get()));
    public static final Supplier<BlockEntityType<GyrofugeInputHatchBlockEntity>> GYROFUGE_INPUT_HATCH_BLOCK_ENTITY = BLOCK_ENTITIES.register("gyrofuge_input_hatch",
            () -> new BlockEntityType<>(GyrofugeInputHatchBlockEntity::new, BlocksRegistration.GYROFUGE_INPUT_HATCH.get()));

    public static final List<Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>>> MELLARIUM_FRAME_HOUSING_ENTITIES = List.of(MELLARIUM_FRAME_HOUSING_1_BLOCK_ENTITY, MELLARIUM_FRAME_HOUSING_2_BLOCK_ENTITY, MELLARIUM_FRAME_HOUSING_3_BLOCK_ENTITY);
}
