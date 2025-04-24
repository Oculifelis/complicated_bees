package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class BeeSorterMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;

    public BeeSorterMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.BEE_SORTER_MENU.get(), windowId, player, 0, 8, 150);
        this.pos = pos;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, BlocksRegistration.BEE_SORTER.get());
    }
}
