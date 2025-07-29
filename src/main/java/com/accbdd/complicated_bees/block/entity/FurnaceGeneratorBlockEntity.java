package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class FurnaceGeneratorBlockEntity extends BaseGeneratorBlockEntity {
    public static final int BASE_GENERATE = ServerConfig.SERVER_CONFIG.furnaceGeneratorBaseEnergy.get();
    public static final int BASE_TRANSFER = ServerConfig.SERVER_CONFIG.furnaceGeneratorBaseTransfer.get();
    public static final int BASE_STORAGE = ServerConfig.SERVER_CONFIG.furnaceGeneratorBaseStorage.get();

    public FurnaceGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistration.FURNACE_GENERATOR_BLOCK_ENTITY.get(), pos, state, BASE_GENERATE, BASE_TRANSFER, BASE_STORAGE);
    }

    @Override
    public boolean isValidInput(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
    }
}
