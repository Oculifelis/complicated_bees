package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.block.entity.*;
import com.accbdd.complicated_bees.block.entity.mellarium.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BlockEntitiesRegistration {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final Supplier<BlockEntityType<ApiaryBlockEntity>> APIARY_ENTITY = BLOCK_ENTITIES.register("apiary",
            () -> BlockEntityType.Builder.of(ApiaryBlockEntity::new, BlocksRegistration.APIARY.get()).build(null));
    public static final Supplier<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE_ENTITY = BLOCK_ENTITIES.register("centrifuge",
            () -> BlockEntityType.Builder.of(CentrifugeBlockEntity::new, BlocksRegistration.CENTRIFUGE.get()).build(null));
    public static final Supplier<BlockEntityType<BeeNestBlockEntity>> BEE_NEST_ENTITY = BLOCK_ENTITIES.register("bee_nest",
            () -> BlockEntityType.Builder.of(BeeNestBlockEntity::new, BlocksRegistration.BEE_NEST.get()).build(null));
    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("generator",
            () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, BlocksRegistration.GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<MicroscopeBlockEntity>> MICROSCOPE_BLOCK_ENTITY = BLOCK_ENTITIES.register("microscope",
            () -> BlockEntityType.Builder.of(MicroscopeBlockEntity::new, BlocksRegistration.MICROSCOPE.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumControllerBlockEntity>> MELLARIUM_CONTROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_controller",
            () -> BlockEntityType.Builder.of(MellariumControllerBlockEntity::new, BlocksRegistration.MELLARIUM_CONTROLLER.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumBaseBlockEntity>> MELLARIUM_BASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_base",
            () -> BlockEntityType.Builder.of(MellariumBaseBlockEntity::new, BlocksRegistration.MELLARIUM_BASE.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumAirConBlockEntity>> MELLARIUM_AIR_CON_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_aircon",
            () -> BlockEntityType.Builder.of(MellariumAirConBlockEntity::new, BlocksRegistration.MELLARIUM_AIR_CON.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_1_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_1",
            () -> BlockEntityType.Builder.of((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 1), BlocksRegistration.MELLARIUM_FRAME_HOUSING_1.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_2_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_2",
            () -> BlockEntityType.Builder.of((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 2), BlocksRegistration.MELLARIUM_FRAME_HOUSING_2.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>> MELLARIUM_FRAME_HOUSING_3_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_frame_housing_3",
            () -> BlockEntityType.Builder.of((pos, state) -> new MellariumFrameHousingBlockEntity(pos, state, 3), BlocksRegistration.MELLARIUM_FRAME_HOUSING_3.get()).build(null));
    public static final Supplier<BlockEntityType<MellariumRainShieldBlockEntity>> MELLARIUM_RAIN_SHIELD_BLOCK_ENTITY = BLOCK_ENTITIES.register("mellarium_rain_shield",
            () -> BlockEntityType.Builder.of(MellariumRainShieldBlockEntity::new, BlocksRegistration.MELLARIUM_RAIN_SHIELD.get()).build(null));
    public static final Supplier<BlockEntityType<SignBlockEntityCB>> CB_SIGN_ENTITY = BLOCK_ENTITIES.register("sign_cb",
            () -> BlockEntityType.Builder.of(SignBlockEntityCB::new, BlocksRegistration.HONEYED_SIGN.get(), BlocksRegistration.HONEYED_WALL_SIGN.get()).build(null));
    public static final Supplier<BlockEntityType<HangingSignBlockEntityCB>> CB_HANGING_SIGN_ENTITY = BLOCK_ENTITIES.register("hanging_sign_cb",
            () -> BlockEntityType.Builder.of(HangingSignBlockEntityCB::new, BlocksRegistration.HONEYED_HANGING_SIGN.get(), BlocksRegistration.HONEYED_WALL_HANGING_SIGN.get()).build(null));


    public static final List<Supplier<BlockEntityType<MellariumFrameHousingBlockEntity>>> MELLARIUM_FRAME_HOUSING_ENTITIES = List.of(MELLARIUM_FRAME_HOUSING_1_BLOCK_ENTITY, MELLARIUM_FRAME_HOUSING_2_BLOCK_ENTITY, MELLARIUM_FRAME_HOUSING_3_BLOCK_ENTITY);
}
