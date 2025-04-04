package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumHydroregulatorBlockEntity;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.OutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;

public class MellariumHydroregulatorMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 8;
    private static final int INV_Y = 79;
    private final Level level;

    public MellariumHydroregulatorMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.MELLARIUM_HYDROREGULATOR_MENU.get(), windowId, player, 2, INV_X, INV_Y);
        if (player.level().getBlockEntity(pos) instanceof MellariumHydroregulatorBlockEntity hydroregulator) {
            addSlot(new SlotItemHandler(hydroregulator.getInputItems(), 0, 52, 40));
            addSlot(new OutputSlot(hydroregulator.getItemHandler().resolve().get(), 1, 107, 40));
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
