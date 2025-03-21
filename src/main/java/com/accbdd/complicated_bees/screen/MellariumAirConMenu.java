package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumAirConBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class MellariumAirConMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 8;
    private static final int INV_Y = 61;

    public MellariumAirConMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.MELLARIUM_AIR_CON_MENU.get(), windowId, player, 1, INV_X, INV_Y);
        if (player.level().getBlockEntity(pos) instanceof MellariumAirConBlockEntity aircon) {
            addSlot(new TagSlot(aircon.getItemHandler().resolve().get(), 0, 80, 24, ItemTagGenerator.AIR_CON_FUEL));
        }
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
