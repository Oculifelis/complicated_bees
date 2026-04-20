package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumMutatorBlockEntity;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MellariumMutatorMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 8;
    private static final int INV_Y = 79;
    private final Level level;

    public MellariumMutatorMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.MELLARIUM_MUTATOR_MENU.get(), windowId, player, 1, INV_X, INV_Y);
        if (player.level().getBlockEntity(pos) instanceof MellariumMutatorBlockEntity mutator) {
            addSlot(new SlotItemHandler(mutator.getItemHandler(), 0, 80, 40));
        }
        layoutPlayerInventorySlots(player.getInventory());
        level = player.level();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public Level getLevel() {
        return level;
    }
}
