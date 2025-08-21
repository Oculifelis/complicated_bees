package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class AnalyzerMenu extends AbstractAnalyzerMenu {
    public static final int SLOT_COUNT = 2;
    private static final String INVENTORY_TAG = "contents";
    private static final int INV_X = 36;
    private static final int INV_Y = 134;

    private final int bagSlot;
    private final ItemStackHandler handler;

    public AnalyzerMenu(int windowId, Player player, int bagSlot) {
        super(MenuRegistration.ANALYZER_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.bagSlot = bagSlot;
        this.handler = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                ItemStack bee = getSlot(1).getItem();
                if (getSlot(0).hasItem()) {
                    if (!bee.isEmpty()) {
                        if (!isBeeAnalyzed()) {
                            bee.getOrCreateTag().putBoolean(BeeItem.ANALYZED_TAG, true);
                            getSlot(0).remove(1);
                        }
                    }
                }
                BreedingTracker tracker = BreedingTracker.getTracker(player);
                tracker.discoverIndividual(bee);
                player.getInventory().getItem(bagSlot).getOrCreateTag().put(INVENTORY_TAG, this.serializeNBT());
            }
        };

        handler.deserializeNBT(player.getInventory().getItem(bagSlot).getOrCreateTag().getCompound(INVENTORY_TAG));
        addSlot(new TagSlot(handler, 0, 225, 8, ItemTagGenerator.ANALYZER_FUEL));
        addSlot(new TagSlot(handler, 1, 225, 26, ItemTagGenerator.BEE));
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public void removed(Player pPlayer) {
        ItemStack analyzer = pPlayer.getInventory().getItem(bagSlot);
        analyzer.getOrCreateTag().put(INVENTORY_TAG, handler.serializeNBT());
        super.removed(pPlayer);
    }

    public static AnalyzerMenu fromNetwork(int windowId, Inventory playerInv) {
        return new AnalyzerMenu(windowId, playerInv.player, playerInv.selected);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        if (this.bagSlot == pSlotId - SLOT_COUNT - 27)
            return;
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }
}
