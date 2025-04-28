package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class BeeSorterMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final byte[] filters;

    public BeeSorterMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, new byte[6]);
    }

    public BeeSorterMenu(int windowId, Inventory inv, BlockPos pos, byte[] initialFilters) {
        super(MenuRegistration.BEE_SORTER_MENU.get(), windowId, inv.player, 0, 8, 150);
        this.pos = pos;
        this.filters = initialFilters;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, BlocksRegistration.BEE_SORTER.get());
    }

    public BlockPos getPos() {
        return pos;
    }

    public byte[] getFilters() {
        return filters;
    }
}
