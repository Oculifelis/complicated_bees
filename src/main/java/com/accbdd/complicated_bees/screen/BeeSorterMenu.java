package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.BeeSorterBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.FakeSpeciesSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class BeeSorterMenu extends AbstractBaseInventoryMenu {
    public static final int SLOT_COUNT = 36;
    private final BlockPos pos;
    private final byte[] filters;

    public BeeSorterMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, new byte[6]);
    }

    public BeeSorterMenu(int windowId, Inventory inv, BlockPos pos, byte[] initialFilters) {
        super(MenuRegistration.BEE_SORTER_MENU.get(), windowId, inv.player, SLOT_COUNT, 8, 150);
        this.pos = pos;
        this.filters = initialFilters;
        if (inv.player.level().getBlockEntity(pos) instanceof BeeSorterBlockEntity sorter) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    addSlot(new FakeSpeciesSlot(45 + 44 * j, 19 + 18 * i, sorter.getSpeciesFilters()[i * 6 + j * 2]));
                    addSlot(new FakeSpeciesSlot(63 + 44 * j, 19 + 18 * i, sorter.getSpeciesFilters()[i * 6 + j * 2 + 1]));
                }
            }
        }

        layoutPlayerInventorySlots(inv);
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
