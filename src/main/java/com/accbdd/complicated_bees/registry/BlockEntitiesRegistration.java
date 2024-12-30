package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

}
