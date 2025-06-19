package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class FurnaceGeneratorMenu extends AbstractGeneratorMenu {
    public FurnaceGeneratorMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.FURNACE_GENERATOR_MENU.get(), windowId, player, pos, BlocksRegistration.FURNACE_GENERATOR.get());
    }
}
