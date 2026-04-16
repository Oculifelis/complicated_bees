package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;

public class LibraryMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 36;
    private static final int INV_Y = 134;
    private static final int SLOT_COUNT = 1;
    private boolean clientDirty = false; //horrible and hacky, do not judge me, it's for optimization

    public LibraryMenu(int windowId, Player player) {
        super(MenuRegistration.LIBRARY_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        addSlot(new TagSlot(new ItemStackHandler(1), 0, 225, 8, ItemTagGenerator.BEE) {
            @Override
            public void setChanged() {
                setClientDirty(true);
            }
        });
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.clearContainer(pPlayer, new SimpleContainer(getSlot(0).getItem()));
    }

    public void setClientDirty(boolean value) {
        clientDirty = value;
    }

    public boolean getClientDirty() {
        return clientDirty;
    }
}
