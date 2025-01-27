package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.GeneratorBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.items.SlotItemHandler;

import static com.accbdd.complicated_bees.block.entity.GeneratorBlockEntity.SLOT;
import static com.accbdd.complicated_bees.block.entity.GeneratorBlockEntity.SLOT_COUNT;

public class GeneratorMenu extends AbstractBaseInventoryMenu {

    private final BlockPos pos;
    private int power;
    private int burnTime;
    private int maxBurnTime;
    private static int INV_X = 8;
    private static int INV_Y = 61;

    public GeneratorMenu(int windowId, Player player, BlockPos pos) {
        super(MenuRegistration.GENERATOR_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof GeneratorBlockEntity generator) {
            addSlot(new SlotItemHandler(generator.getItems(), SLOT, 80, 31));
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getStoredPower() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GeneratorMenu.this.power = (GeneratorMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (generator.getStoredPower() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GeneratorMenu.this.power = (GeneratorMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getBurnTime();
                }

                @Override
                public void set(int pValue) {
                    GeneratorMenu.this.burnTime = pValue;
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getMaxBurnTime();
                }

                @Override
                public void set(int pValue) {
                    GeneratorMenu.this.maxBurnTime = pValue;
                }
            });
        }
        layoutPlayerInventorySlots(player.getInventory());
    }

    public int getPower() {
        return power;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlocksRegistration.GENERATOR.get());
    }
}
