package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumFrameHousingBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class MellariumFrameHousingMenu extends AbstractBaseInventoryMenu {
    private static final int INV_X = 8;
    private static final int INV_Y = 61;

    public MellariumFrameHousingMenu(int windowId, Player player, BlockPos pos, int slotCount) {
        super(MenuRegistration.MELLARIUM_FRAME_MENUS.get(slotCount-1).get(), windowId, player, slotCount, INV_X, INV_Y);
        if (player.level().getBlockEntity(pos) instanceof MellariumFrameHousingBlockEntity mellarium) {
            for (int i = 0; i < slotCount; i++) {
                addSlot(new TagSlot(mellarium.getFrameItemHandler(), i, (89 - slotCount*9) + (18 * i), 24, ItemTagGenerator.FRAME));
            }
        }
        layoutPlayerInventorySlots(player.getInventory());
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
