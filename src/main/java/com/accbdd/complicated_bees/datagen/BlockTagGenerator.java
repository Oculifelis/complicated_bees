package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.registry.BlocksRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BlockTagGenerator extends BlockTagsProvider {
    public static final TagKey<Block> SCOOPABLE = BlockTags.create(new ResourceLocation("complicated_bees:mineable/scoop_tool"));
    public static final TagKey<Block> MELLARIUM = BlockTags.create(new ResourceLocation("complicated_bees:multiblock/mellarium"));

    public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(SCOOPABLE).add(BlocksRegistration.BEE_NEST.get());
        tag(MELLARIUM).add(
                BlocksRegistration.MELLARIUM_BASE.get(),
                BlocksRegistration.MELLARIUM_TEMP_UNIT.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_1.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_2.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_3.get(),
                BlocksRegistration.MELLARIUM_CONTROLLER.get(),
                BlocksRegistration.MELLARIUM_RAIN_SHIELD.get(),
                BlocksRegistration.MELLARIUM_MUTATOR.get(),
                BlocksRegistration.MELLARIUM_HYDROREGULATOR.get()
        );
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                BlocksRegistration.APIARY.get(),
                BlocksRegistration.MELLARIUM_BASE.get(),
                BlocksRegistration.MELLARIUM_TEMP_UNIT.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_1.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_2.get(),
                BlocksRegistration.MELLARIUM_FRAME_HOUSING_3.get(),
                BlocksRegistration.MELLARIUM_RAIN_SHIELD.get(),
                BlocksRegistration.MELLARIUM_MUTATOR.get(),
                BlocksRegistration.MELLARIUM_HYDROREGULATOR.get(),
                BlocksRegistration.HONEYED_PLANKS.get(),
                BlocksRegistration.HONEYED_STAIRS.get(),
                BlocksRegistration.HONEYED_SLAB.get(),
                BlocksRegistration.HONEYED_FENCE.get(),
                BlocksRegistration.HONEYED_FENCE_GATE.get(),
                BlocksRegistration.HONEYED_BUTTON.get(),
                BlocksRegistration.HONEYED_PRESSURE_PLATE.get(),
                BlocksRegistration.HONEYED_DOOR.get(),
                BlocksRegistration.HONEYED_TRAPDOOR.get(),
                BlocksRegistration.HONEYED_SIGN.get(),
                BlocksRegistration.HONEYED_WALL_SIGN.get(),
                BlocksRegistration.HONEYED_HANGING_SIGN.get(),
                BlocksRegistration.HONEYED_WALL_HANGING_SIGN.get()
        );
        tag(BlockTags.PLANKS).add(BlocksRegistration.HONEYED_PLANKS.get());
        tag(BlockTags.WOODEN_STAIRS).add(BlocksRegistration.HONEYED_STAIRS.get());
        tag(BlockTags.WOODEN_SLABS).add(BlocksRegistration.HONEYED_SLAB.get());
        tag(BlockTags.WOODEN_FENCES).add(BlocksRegistration.HONEYED_FENCE.get());
        tag(Tags.Blocks.FENCE_GATES_WOODEN).add(BlocksRegistration.HONEYED_FENCE_GATE.get());
        tag(BlockTags.WOODEN_BUTTONS).add(BlocksRegistration.HONEYED_BUTTON.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(BlocksRegistration.HONEYED_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_DOORS).add(BlocksRegistration.HONEYED_DOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(BlocksRegistration.HONEYED_TRAPDOOR.get());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(BlocksRegistration.HONEYED_HANGING_SIGN.get());
        tag(BlockTags.WALL_SIGNS).add(BlocksRegistration.HONEYED_WALL_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(BlocksRegistration.HONEYED_WALL_HANGING_SIGN.get());
        tag(BlockTags.STANDING_SIGNS).add(BlocksRegistration.HONEYED_SIGN.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                BlocksRegistration.CENTRIFUGE.get(),
                BlocksRegistration.CHISELED_WAX.get(),
                BlocksRegistration.APID_LIBRARY.get(),
                BlocksRegistration.WAX_BLOCK.get(),
                BlocksRegistration.WAX_BLOCK_STAIRS.get(),
                BlocksRegistration.WAX_BLOCK_SLAB.get(),
                BlocksRegistration.WAX_BLOCK_WALL.get(),
                BlocksRegistration.SMOOTH_WAX.get(),
                BlocksRegistration.SMOOTH_WAX_STAIRS.get(),
                BlocksRegistration.SMOOTH_WAX_SLAB.get(),
                BlocksRegistration.SMOOTH_WAX_WALL.get(),
                BlocksRegistration.WAX_BRICKS.get(),
                BlocksRegistration.WAX_BRICK_STAIRS.get(),
                BlocksRegistration.WAX_BRICK_SLAB.get(),
                BlocksRegistration.WAX_BRICK_WALL.get(),
                BlocksRegistration.CHISELED_WAX.get()
        );
        tag(BlockTags.WALLS).add(
                BlocksRegistration.WAX_BLOCK_WALL.get(),
                BlocksRegistration.SMOOTH_WAX_WALL.get(),
                BlocksRegistration.WAX_BRICK_WALL.get()
        );
        tag(BlockTags.STAIRS).add(
                BlocksRegistration.WAX_BLOCK_STAIRS.get(),
                BlocksRegistration.SMOOTH_WAX_STAIRS.get(),
                BlocksRegistration.WAX_BRICK_STAIRS.get()
        );
        tag(BlockTags.SLABS).add(
                BlocksRegistration.WAX_BLOCK_SLAB.get(),
                BlocksRegistration.SMOOTH_WAX_SLAB.get(),
                BlocksRegistration.WAX_BRICK_SLAB.get()
        );
    }
}
