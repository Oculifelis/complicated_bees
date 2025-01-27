package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.ItemStackHandler;

public class LibraryMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 36;
    private static final int INV_Y = 134;
    private static final int SLOT_COUNT = 1;

    public LibraryMenu(int windowId, Player player) {
        super(MenuRegistration.LIBRARY_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        addSlot(new TagSlot(new ItemStackHandler(1), 0, 225, 8, ItemTagGenerator.BEE));
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
