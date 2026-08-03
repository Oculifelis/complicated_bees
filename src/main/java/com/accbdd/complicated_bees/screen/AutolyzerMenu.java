package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.AutolyzerBlockEntity;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class AutolyzerMenu extends AbstractAnalyzerMenu {
    private static final int INV_X = 36;
    private static final int INV_Y = 134;
    private final BlockPos pos;

    public AutolyzerMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.AUTOLYZER_MENU.get(), windowId, player, 2, INV_X, INV_Y);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof AutolyzerBlockEntity autolyzer) {
            var handler = autolyzer.getItems();
            addSlot(new TagSlot(handler, 0, 225, 8, ItemTagGenerator.ANALYZER_FUEL));
            addSlot(new TagSlot(handler, 1, 225, 26, ItemTagGenerator.BEE));
        }
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlocksRegistration.AUTOLYZER.get());
    }

    @Override
    public boolean isBeeAnalyzed() {
        ItemStack bee = getSlot(1).getItem();
        return bee.is(ItemTagGenerator.BEE) && bee.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).analyzed();
    }
}
