package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.block.entity.gyrofuge.AbstractGyrofugeBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeControllerBlockEntity;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.SlotItemHandler;

public class GyrofugeMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final ContainerData data;
    private final static int INV_X = 8;
    private static final int INV_Y = 84;

    private int power;
    private int maxPower;
    protected MachineModifier modifier;

    public GyrofugeMenu(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(3));
    }

    public GyrofugeMenu(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(MenuRegistration.GYROFUGE_MENU.get(), windowId, player, GyrofugeControllerBlockEntity.SLOT_COUNT, INV_X, INV_Y);
        this.data = data;
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof AbstractGyrofugeBlockEntity blockEntity) {
            GyrofugeControllerBlockEntity controller;
            if (blockEntity.getLogic() != null) {
                controller = blockEntity.getLogic().getController();
            } else {
                controller = MultiblockHelper.tryBuildGyrofuge(player.level(), pos).getController();
            }
            modifier = controller.getGyrofugeLogic().getMachineModifier();
            addSlot(new SlotItemHandler(controller.getInputItems(), 0, 15, 26));
            addSlot(new SlotItemHandler(controller.getInputItems(), 1, 33, 26));
            addSlot(new SlotItemHandler(controller.getInputItems(), 2, 15, 44));
            addSlot(new SlotItemHandler(controller.getInputItems(), 3, 33, 44));
            for (int i = 0; i < 9; i++) {
                addSlot(new SlotItemHandler(controller.getOutputItemHandler().resolve().get(),
                        i,
                        91 + (18 * (i % 3)),
                        17 + (18 * (i / 3))));
            }
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().get().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.power = (GyrofugeMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (controller.getEnergyHandler().resolve().get().getEnergyStored() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.power = (GyrofugeMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().get().getMaxEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.maxPower = (GyrofugeMenu.this.maxPower & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (controller.getEnergyHandler().resolve().get().getMaxEnergyStored() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.maxPower = (GyrofugeMenu.this.maxPower & 0xffff) | ((pValue & 0xffff) << 16);
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

    public int getMaxPower() {
        return maxPower;
    }

    public int getScaledProgress() {
        int progress = getProgress();
        int maxProgress = this.data.get(1);
        int progressArrowSize = 20;

        return Math.min(maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0, progressArrowSize);
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getPowerUsage() {
        return this.data.get(2);
    }

    @Override
    public boolean stillValid(Player player) {
        return ContainerLevelAccess.create(player.level(), pos).evaluate((level, pos1) -> !level.getBlockState(pos1).is(Blocks.AIR)).get() && player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
    }
}
