package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.mellarium.AbstractMellariumBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.MenuRegistration;
import com.accbdd.complicated_bees.screen.slot.ItemSlot;
import com.accbdd.complicated_bees.screen.slot.OutputSlot;
import com.accbdd.complicated_bees.screen.slot.TagSlot;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import static com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity.*;

public class MellariumMenu extends AbstractBaseInventoryMenu {
    private final BlockPos pos;
    private final ContainerData data;
    private static final int INV_X = 8;
    private static final int INV_Y = 105;

    private int power;
    private int maxPower;

    public MellariumMenu(int windowId, Player player, BlockPos pos) {
        this(windowId, player, pos, new SimpleContainerData(3));
    }

    public MellariumMenu(int windowId, Player player, BlockPos pos, ContainerData data) {
        super(MenuRegistration.MELLARIUM_MENU.get(), windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.data = data;
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof AbstractMellariumBlockEntity blockEntity) {
            MellariumControllerBlockEntity controller;
            if (blockEntity.getLogic() != null) {
                controller = blockEntity.getLogic().getController();
            } else {
                controller = MultiblockHelper.tryBuildMellarium(player.level(), pos, player.getUUID()).getController();
            }
            addSlot(new TagSlot(controller.getBeeItems(), BEE_SLOT, 29, 38, ItemTagGenerator.ROYAL));
            addSlot(new ItemSlot(controller.getBeeItems(), BEE_SLOT + 1, 29, 63, ItemsRegistration.DRONE.get()));

            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT, 115, 51));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 1, 115, 26));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 2, 137, 39));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 3, 137, 64));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 4, 115, 76));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 5, 93, 64));
            addSlot(new OutputSlot(controller.getOutputItems(), OUTPUT_SLOT + 6, 93, 39));
            
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().get().getEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    MellariumMenu.this.power = (MellariumMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (controller.getEnergyHandler().resolve().get().getEnergyStored() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    MellariumMenu.this.power = (MellariumMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return controller.getEnergyHandler().resolve().get().getMaxEnergyStored() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    MellariumMenu.this.maxPower = (MellariumMenu.this.maxPower & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (controller.getEnergyHandler().resolve().get().getMaxEnergyStored() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    MellariumMenu.this.maxPower = (MellariumMenu.this.maxPower & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
        }
        layoutPlayerInventorySlots(player.getInventory());

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return ContainerLevelAccess.create(player.level(), pos).evaluate((level, pos1) -> !level.getBlockState(pos1).is(Blocks.AIR)).get() && player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
    }

    public boolean hasQueen() {
        ItemStack item = getSlot(0).getItem();
        return item.getItem() == ItemsRegistration.QUEEN.get();
    }

    public int getScaledProgress(float progress, int maxProgress) {
        int barHeight = 45;
        return maxProgress != 0 && progress != 0 ? (int) (progress * barHeight / maxProgress) : 0;
    }

    public ItemStack getQueen() {
        return getSlot(0).getItem();
    }

    public boolean isBreeding() {
        return data.get(0) > 0;
    }

    public ContainerData getData() {
        return this.data;
    }

    public boolean hasQueue() {
        return data.get(2) == EnumErrorCodes.OUTPUT_FULL.value;
    }

    public int getPower() {
        return power;
    }

    public int getMaxPower() {
        return maxPower;
    }
}
