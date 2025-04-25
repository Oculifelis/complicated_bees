package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.BeeSorterBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class BeeSorterMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private ContainerData data;

    public BeeSorterMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.BEE_SORTER_MENU.get(), windowId, player, 0, 8, 150);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof BeeSorterBlockEntity sorter) {
            this.data = new ContainerData() {
                @Override
                public int get(int pIndex) {
                    return sorter.getTypeFilters()[pIndex];
                }

                @Override
                public void set(int pIndex, int pValue) {
                    sorter.setTypeFilters(pIndex, (byte)pValue);
                }

                @Override
                public int getCount() {
                    return 6;
                }
            };
            addDataSlots(data);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, BlocksRegistration.BEE_SORTER.get());
    }

    public ContainerData getData() {
        return data;
    }
}
