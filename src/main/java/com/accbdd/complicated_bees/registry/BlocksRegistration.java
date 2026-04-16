package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.block.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BlocksRegistration {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final BlockBehaviour.Properties WAX_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F);
    public static final BlockBehaviour.Properties HONEYPLANK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .instrument(NoteBlockInstrument.BASS)
            .sound(SoundType.WOOD)
            .strength(2, 3);
    public static final WoodType HONEYED_WOOD = new WoodType(MODID + ":honeyed", BlockSetType.OAK);

    public static final DeferredBlock<BeeNestBlock> BEE_NEST = BLOCKS.register("bee_nest", BeeNestBlock::new);
    public static final DeferredBlock<ApiaryBlock> APIARY = BLOCKS.register("apiary", ApiaryBlock::new);
    public static final DeferredBlock<CentrifugeBlock> CENTRIFUGE = BLOCKS.register("centrifuge", CentrifugeBlock::new);
    public static final DeferredBlock<FurnaceGeneratorBlock> FURNACE_GENERATOR = BLOCKS.register("furnace_generator", FurnaceGeneratorBlock::new);
    public static final DeferredBlock<HoneyGeneratorBlock> HONEY_GENERATOR = BLOCKS.register("honey_generator", HoneyGeneratorBlock::new);
    public static final DeferredBlock<MicroscopeBlock> MICROSCOPE = BLOCKS.register("microscope", MicroscopeBlock::new);
    public static final DeferredBlock<BeeSorterBlock> BEE_SORTER = BLOCKS.register("bee_sorter", BeeSorterBlock::new);
    public static final DeferredBlock<AutolyzerBlock> AUTOLYZER = BLOCKS.register("autolyzer", AutolyzerBlock::new);

    public static final DeferredBlock<MellariumBlock> MELLARIUM_BASE = BLOCKS.register("mellarium_base", () -> new MellariumBlock(MellariumBlock.MellariumBlockType.BASE));
    public static final DeferredBlock<MellariumBlock> MELLARIUM_TEMP_UNIT = BLOCKS.register("mellarium_temp_unit", MellariumTempUnitBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_FRAME_HOUSING_1 = BLOCKS.register("mellarium_frame_housing_1", () -> new MellariumFrameHousingBlock(1));
    public static final DeferredBlock<MellariumBlock> MELLARIUM_FRAME_HOUSING_2 = BLOCKS.register("mellarium_frame_housing_2", () -> new MellariumFrameHousingBlock(2));
    public static final DeferredBlock<MellariumBlock> MELLARIUM_FRAME_HOUSING_3 = BLOCKS.register("mellarium_frame_housing_3", () -> new MellariumFrameHousingBlock(3));
    public static final DeferredBlock<MellariumBlock> MELLARIUM_RAIN_SHIELD = BLOCKS.register("mellarium_rain_shield", MellariumRainShieldBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_MUTATOR = BLOCKS.register("mellarium_mutator", MellariumMutatorBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_HYDROREGULATOR = BLOCKS.register("mellarium_hydroregulator", MellariumHydroregulatorBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_ENERGY_CELL = BLOCKS.register("mellarium_energy_cell", MellariumEnergyCellBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_SKYBOX = BLOCKS.register("mellarium_skybox", MellariumSkyboxBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_TEMPORAL_SIMULATOR = BLOCKS.register("mellarium_temporal_simulator", MellariumTemporalSimulatorBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_OUTPUT_HATCH = BLOCKS.register("mellarium_output_hatch", MellariumOutputHatchBlock::new);
    public static final DeferredBlock<MellariumBlock> MELLARIUM_CONTROLLER = BLOCKS.register("mellarium_controller", () -> new MellariumBlock(MellariumBlock.MellariumBlockType.CONTROLLER));

    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_BASE = BLOCKS.register("gyrofuge_base", () -> new GyrofugeBlock(GyrofugeBlock.GyrofugeBlockType.BASE));
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_CONTROLLER = BLOCKS.register("gyrofuge_controller", () -> new GyrofugeBlock(GyrofugeBlock.GyrofugeBlockType.CONTROLLER));
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_ENERGY_CELL = BLOCKS.register("gyrofuge_energy_cell", GyrofugeEnergyCellBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_BASIC_PROCESSING_UNIT = BLOCKS.register("gyrofuge_basic_processing_unit", GyrofugeBasicProcessingUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_PROCESSING_UNIT = BLOCKS.register("gyrofuge_processing_unit", GyrofugeProcessingUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_ADVANCED_PROCESSING_UNIT = BLOCKS.register("gyrofuge_advanced_processing_unit", GyrofugeAdvancedProcessingUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_SPEED_UNIT = BLOCKS.register("gyrofuge_speed_unit", GyrofugeSpeedUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_EFFICIENCY_UNIT = BLOCKS.register("gyrofuge_efficiency_unit", GyrofugeEfficiencyUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_EXTRACTION_UNIT = BLOCKS.register("gyrofuge_extraction_unit", GyrofugeExtractionUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_OUTPUT_HATCH = BLOCKS.register("gyrofuge_output_hatch", GyrofugeOutputHatchBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_INPUT_HATCH = BLOCKS.register("gyrofuge_input_hatch", GyrofugeInputHatchBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_BASIC_SPEED_UNIT = BLOCKS.register("gyrofuge_basic_speed_unit", GyrofugeBasicSpeedUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_ADVANCED_SPEED_UNIT = BLOCKS.register("gyrofuge_advanced_speed_unit", GyrofugeAdvancedSpeedUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_BASIC_EFFICIENCY_UNIT = BLOCKS.register("gyrofuge_basic_efficiency_unit", GyrofugeBasicEfficiencyUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_ADVANCED_EFFICIENCY_UNIT = BLOCKS.register("gyrofuge_advanced_efficiency_unit", GyrofugeAdvancedEfficiencyUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_BASIC_EXTRACTION_UNIT = BLOCKS.register("gyrofuge_basic_extraction_unit", GyrofugeBasicExtractionUnitBlock::new);
    public static final DeferredBlock<GyrofugeBlock> GYROFUGE_ADVANCED_EXTRACTION_UNIT = BLOCKS.register("gyrofuge_advanced_extraction_unit", GyrofugeAdvancedExtractionUnitBlock::new);

    public static final DeferredBlock<Block> APID_LIBRARY = BLOCKS.register("apid_library", ApidLibraryBlock::new);

    public static final DeferredBlock<Block> WAX_BLOCK = BLOCKS.register("wax_block", () -> new Block(WAX_PROPERTIES));
    public static final DeferredBlock<StairBlock> WAX_BLOCK_STAIRS = BLOCKS.register("wax_block_stairs", () -> stair(WAX_BLOCK.get()));
    public static final DeferredBlock<SlabBlock> WAX_BLOCK_SLAB = BLOCKS.register("wax_block_slab", () -> slab(WAX_BLOCK.get()));
    public static final DeferredBlock<WallBlock> WAX_BLOCK_WALL = BLOCKS.register("wax_block_wall", () -> wall(WAX_BLOCK.get()));
    public static final DeferredBlock<Block> SMOOTH_WAX = BLOCKS.register("smooth_wax", () -> new Block(WAX_PROPERTIES));
    public static final DeferredBlock<StairBlock> SMOOTH_WAX_STAIRS = BLOCKS.register("smooth_wax_stairs", () -> stair(SMOOTH_WAX.get()));
    public static final DeferredBlock<SlabBlock> SMOOTH_WAX_SLAB = BLOCKS.register("smooth_wax_slab", () -> slab(SMOOTH_WAX.get()));
    public static final DeferredBlock<WallBlock> SMOOTH_WAX_WALL = BLOCKS.register("smooth_wax_wall", () -> wall(SMOOTH_WAX.get()));
    public static final DeferredBlock<Block> WAX_BRICKS = BLOCKS.register("wax_bricks", () -> new Block(WAX_PROPERTIES));
    public static final DeferredBlock<StairBlock> WAX_BRICK_STAIRS = BLOCKS.register("wax_brick_stairs", () -> stair(WAX_BRICKS.get()));
    public static final DeferredBlock<SlabBlock> WAX_BRICK_SLAB = BLOCKS.register("wax_brick_slab", () -> slab(WAX_BRICKS.get()));
    public static final DeferredBlock<WallBlock> WAX_BRICK_WALL = BLOCKS.register("wax_brick_wall", () -> wall(WAX_BRICKS.get()));
    public static final DeferredBlock<Block> CHISELED_WAX = BLOCKS.register("chiseled_wax", () -> new Block(WAX_PROPERTIES));

    public static final DeferredBlock<Block> HONEYED_PLANKS = BLOCKS.register("honeyed_planks", () -> new Block(HONEYPLANK_PROPERTIES));
    public static final DeferredBlock<StairBlock> HONEYED_STAIRS = BLOCKS.register("honeyed_stairs", () -> stair(HONEYED_PLANKS.get()));
    public static final DeferredBlock<SlabBlock> HONEYED_SLAB = BLOCKS.register("honeyed_slab", () -> slab(HONEYED_PLANKS.get()));
    public static final DeferredBlock<FenceBlock> HONEYED_FENCE = BLOCKS.register("honeyed_fence", () -> fence(HONEYED_PLANKS.get()));
    public static final DeferredBlock<FenceGateBlock> HONEYED_FENCE_GATE = BLOCKS.register("honeyed_fence_gate", () -> gate(HONEYED_PLANKS.get()));
    public static final DeferredBlock<ButtonBlock> HONEYED_BUTTON = BLOCKS.register("honeyed_button", () -> button(BlockSetType.OAK, 30, HONEYED_PLANKS.get()));
    public static final DeferredBlock<PressurePlateBlock> HONEYED_PRESSURE_PLATE = BLOCKS.register("honeyed_pressure_plate", () -> plate(BlockSetType.OAK, HONEYED_PLANKS.get()));
    public static final DeferredBlock<DoorBlock> HONEYED_DOOR = BLOCKS.register("honeyed_door", () -> door(BlockSetType.OAK, HONEYED_PLANKS.get()));
    public static final DeferredBlock<TrapDoorBlock> HONEYED_TRAPDOOR = BLOCKS.register("honeyed_trapdoor", () -> trapdoor(BlockSetType.OAK, HONEYED_PLANKS.get()));
    public static final DeferredBlock<StandingSignBlock> HONEYED_SIGN = BLOCKS.register("honeyed_sign", () -> sign(HONEYED_WOOD, HONEYED_PLANKS.get()));
    public static final DeferredBlock<WallSignBlock> HONEYED_WALL_SIGN = BLOCKS.register("honeyed_wall_sign", () -> wallSign(HONEYED_WOOD, HONEYED_PLANKS.get()));
    public static final DeferredBlock<WallHangingSignBlock> HONEYED_WALL_HANGING_SIGN = BLOCKS.register("honeyed_wall_hanging_sign", () -> wallHangingSign(HONEYED_WOOD, HONEYED_PLANKS.get()));
    public static final DeferredBlock<CeilingHangingSignBlock> HONEYED_HANGING_SIGN = BLOCKS.register("honeyed_hanging_sign", () -> hangingSign(HONEYED_WOOD, HONEYED_PLANKS.get()));

    private static StairBlock stair(Block base) {
        return new StairBlock(base.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static SlabBlock slab(Block base) {
        return new SlabBlock(BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static WallBlock wall(Block base) {
        return new WallBlock(BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static FenceBlock fence(Block base) {
        return new FenceBlock(BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static FenceGateBlock gate(Block base) {
        return new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static ButtonBlock button(BlockSetType type, int ticksToPress, Block base) {
        return new ButtonBlock(type, ticksToPress, BlockBehaviour.Properties.ofFullCopy(base).noCollission());
    }

    private static PressurePlateBlock plate(BlockSetType type, Block base) {
        return new PressurePlateBlock(type, BlockBehaviour.Properties.ofFullCopy(base));
    }

    private static DoorBlock door(BlockSetType type, Block base) {
        return new DoorBlock(type, BlockBehaviour.Properties.ofFullCopy(base).noOcclusion());
    }

    private static TrapDoorBlock trapdoor(BlockSetType type, Block base) {
        return new TrapDoorBlock(type, BlockBehaviour.Properties.ofFullCopy(base).noOcclusion());
    }

    private static StandingSignBlock sign(WoodType type, Block base) {
        return new StandingSignBlockCB(BlockBehaviour.Properties.ofFullCopy(base).noOcclusion(), type);
    }

    private static WallSignBlock wallSign(WoodType type, Block base) {
        return new WallSignBlockCB(BlockBehaviour.Properties.ofFullCopy(base).noOcclusion(), type);
    }

    private static WallHangingSignBlock wallHangingSign(WoodType type, Block base) {
        return new WallHangingSignBlockCB(BlockBehaviour.Properties.ofFullCopy(base).noOcclusion(), type);
    }

    private static CeilingHangingSignBlock hangingSign(WoodType type, Block base) {
        return new CeilingHangingSignBlockCB(BlockBehaviour.Properties.ofFullCopy(base).noOcclusion(), type);
    }
}
