package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.CentrifugeBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import static com.accbdd.complicated_bees.block.entity.CentrifugeBlockEntity.*;

public class CentrifugeMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final ContainerData data;
    private final static int INV_X = 8;
    private static final int INV_Y = 84;

    private int power;

    public CentrifugeMenu(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(2));
    }

    public CentrifugeMenu(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(MenuRegistration.CENTRIFUGE_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.data = data;
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof CentrifugeBlockEntity centrifuge) {
            addSlot(new SlotItemHandler(centrifuge.getInputItems(), INPUT_SLOT, 34, 35));
            for (int i = 0; i < 9; i++) {
                addSlot(new SlotItemHandler(centrifuge.getOutputItems(),
                        OUTPUT_SLOT + i,
                        91 + (18 * (i % 3)),
                        17 + (18 * (i / 3))) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return centrifuge.getStoredPower() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    CentrifugeMenu.this.power = (CentrifugeMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (centrifuge.getStoredPower() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    CentrifugeMenu.this.power = (CentrifugeMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
        }
        layoutPlayerInventorySlots(player.getInventory());

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getPower() {
        return power;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 20;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlocksRegistration.CENTRIFUGE.get());
    }
}
