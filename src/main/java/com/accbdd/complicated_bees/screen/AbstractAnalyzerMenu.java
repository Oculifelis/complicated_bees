package com.accbdd.complicated_bees.screen;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

/**
 * marker class for the analyzer widget
 */
public abstract class AbstractAnalyzerMenu extends AbstractBaseInventoryMenu {
    public AbstractAnalyzerMenu(MenuType<?> menuType, int windowId, Player player, int slotCount, int invX, int invY) {
        super(menuType, windowId, player, slotCount, invX, invY);
    }

    abstract public boolean isBeeAnalyzed();
}
