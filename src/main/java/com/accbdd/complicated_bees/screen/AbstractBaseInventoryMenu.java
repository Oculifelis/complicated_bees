package com.accbdd.complicated_bees.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractBaseInventoryMenu extends AbstractContainerMenu {
    private final int slotCount, invX, invY;
    public AbstractBaseInventoryMenu(MenuType<?> menuType, int windowId, Player player, int slotCount, int invX, int invY) {
        super(menuType, windowId);
        this.slotCount = slotCount;
        this.invX = invX;
        this.invY = invY;
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private void addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
    }

    protected void layoutPlayerInventorySlots(Container playerInventory) {
        // inv
        addSlotBox(playerInventory, 9, invX, invY, 9, 18, 3, 18);
        // hotbar
        addSlotRange(playerInventory, 0, invX, invY + 58, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        //todo: quick moving stuff makes it duplicate on load....
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < slotCount) {
                if (!this.moveItemStackTo(stack, slotCount, Inventory.INVENTORY_SIZE + slotCount, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, 0, slotCount, false)) {
                if (index < 27 + slotCount) {
                    if (!this.moveItemStackTo(stack, 27 + slotCount, 36 + slotCount, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + slotCount && !this.moveItemStackTo(stack, slotCount, 27 + slotCount, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }
}
