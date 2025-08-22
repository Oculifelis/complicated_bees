package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.item.BeeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

/**
 * marker class for the analyzer widget
 */
public abstract class AbstractAnalyzerMenu extends AbstractBaseInventoryMenu {
    public AbstractAnalyzerMenu(MenuType<?> menuType, int windowId, Player player, int slotCount, int invX, int invY) {
        super(menuType, windowId, player, slotCount, invX, invY);
    }

    public boolean isBeeAnalyzed() {
        ItemStack bee = getSlot(1).getItem();
        return bee.is(ItemTagGenerator.BEE) && bee.getOrCreateTag().getBoolean(BeeItem.ANALYZED_TAG);
    }
}
