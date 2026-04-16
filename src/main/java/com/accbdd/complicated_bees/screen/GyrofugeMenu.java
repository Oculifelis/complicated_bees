package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.block.entity.gyrofuge.AbstractGyrofugeBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeControllerBlockEntity;
import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import com.accbdd.complicated_bees.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.SimpleContainerData;

import java.util.Optional;

public class GyrofugeMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final ContainerData data;
    private final static int INV_X = 8;
    private static final int INV_Y = 84;

    private int power;
    private int maxPower;
    protected MachineModifier modifier;
    private GyrofugeLogic logic;

    public GyrofugeMenu(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(3));
    }

    public GyrofugeMenu(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(MenuRegistration.GYROFUGE_MENU.get(), windowId, player, GyrofugeControllerBlockEntity.SLOT_COUNT, INV_X, INV_Y);
        this.data = data;
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof AbstractGyrofugeBlockEntity blockEntity) {
            if (blockEntity.getLogic() != null) {
                logic = blockEntity.getLogic();
            } else {
                logic = MultiblockHelper.tryBuildGyrofuge(player.level(), pos);
            }
            if (logic == null) return;
            Optional<GyrofugeControllerBlockEntity> controllerOptional = logic.getController();
            if (controllerOptional.isEmpty()) return;
            modifier = logic.getMachineModifier();
            GyrofugeControllerBlockEntity controller = controllerOptional.get();
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
                    return controller.getEnergyHandler().resolve().
                            map(iEnergyStorage -> iEnergyStorage.getEnergyStored() & 0xffff).
                            orElse(0);
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.power = (GyrofugeMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().
                            map(iEnergyStorage -> (iEnergyStorage.getEnergyStored() >> 16) & 0xffff).
                            orElse(0);
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.power = (GyrofugeMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().
                            map(iEnergyStorage -> iEnergyStorage.getMaxEnergyStored() & 0xffff).
                            orElse(0);
                }

                @Override
                public void set(int pValue) {
                    GyrofugeMenu.this.maxPower = (GyrofugeMenu.this.maxPower & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().
                            map(iEnergyStorage -> (iEnergyStorage.getMaxEnergyStored() >> 16) & 0xffff).
                            orElse(0);
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
            return logic != null && logic.getController().
                    filter(controller -> MultiblockHelper.isValidGyrofuge(controller.getLevel(), logic.getCenter()) && Util.canReach(pos, player)).isPresent();
    }
}
