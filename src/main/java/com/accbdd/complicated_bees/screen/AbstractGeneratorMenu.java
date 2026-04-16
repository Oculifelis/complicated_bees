package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.block.entity.BaseGeneratorBlockEntity;
import com.accbdd.complicated_bees.item.UpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.block.entity.BaseGeneratorBlockEntity.SLOT;
import static com.accbdd.complicated_bees.block.entity.BaseGeneratorBlockEntity.SLOT_COUNT;

public abstract class AbstractGeneratorMenu extends AbstractBaseInventoryMenu {

    private final BlockPos pos;
    private final Block block;
    private int power;
    private int burnTime;
    private int maxBurnTime;
    private int generate;
    private List<Slot> upgradeSlots;
    private static final int INV_X = 8;
    private static final int INV_Y = 76;

    public AbstractGeneratorMenu(MenuType<?> type, int windowId, Player player, BlockPos pos, Block block) {
        super(type, windowId, player, SLOT_COUNT, INV_X, INV_Y);
        this.pos = pos;
        this.block = block;

        if (player.level().getBlockEntity(pos) instanceof BaseGeneratorBlockEntity generator) {
            this.upgradeSlots = new ArrayList<>();
            addSlot(new SlotItemHandler(generator.getItems(), SLOT, 80, 40));
            for (int i = 0; i < 3; i++) {
                upgradeSlots.add(addSlot(new SlotItemHandler(generator.getUpgradeItemHandler().resolve().get(), i,
                        145,
                        8 + i * 18)));
            }

            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getStoredPower() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    AbstractGeneratorMenu.this.power = (AbstractGeneratorMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (generator.getStoredPower() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    AbstractGeneratorMenu.this.power = (AbstractGeneratorMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getCurrentBurnTime();
                }

                @Override
                public void set(int pValue) {
                    AbstractGeneratorMenu.this.burnTime = pValue;
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getMaxBurnTime();
                }

                @Override
                public void set(int pValue) {
                    AbstractGeneratorMenu.this.maxBurnTime = pValue;
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return generator.getGenerate();
                }

                @Override
                public void set(int pValue) {
                    AbstractGeneratorMenu.this.generate = pValue;
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

    public int getPowerPerTick() {
        return generate;
    }

    public List<Component> getUpgradeTooltip() {
        return MachineModifier.of(upgradeSlots.stream().map(slot -> {
            if (slot.hasItem() && slot.getItem().getItem() instanceof UpgradeItem upgrade) {
                return upgrade.getModifier();
            } else {
                return MachineModifier.BLANK;
            }
        }).toArray(MachineModifier[]::new)).getTooltipComponents();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, block);
    }
}
