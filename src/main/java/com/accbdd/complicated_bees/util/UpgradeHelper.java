package com.accbdd.complicated_bees.util;

import com.accbdd.complicated_bees.item.UpgradeItem;
import net.minecraftforge.items.IItemHandler;

public class UpgradeHelper {
    public static float getSpeedMod(IItemHandler upgradeItems) {
        float mod = 1f;
        for (int i = 0; i < upgradeItems.getSlots(); i++) {
            if (upgradeItems.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                mod *= upgrade.getSpeedMod();
            }
        }
        return mod;
    }

    public static float getEfficiencyMod(IItemHandler upgradeItems) {
        float mod = 1f;
        for (int i = 0; i < upgradeItems.getSlots(); i++) {
            if (upgradeItems.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                mod *= upgrade.getEfficiencyMod();
            }
        }
        return mod;
    }

    public static float getOutputMod(IItemHandler upgradeItems) {
        float mod = 1f;
        for (int i = 0; i < upgradeItems.getSlots(); i++) {
            if (upgradeItems.getStackInSlot(i).getItem() instanceof UpgradeItem upgrade) {
                mod *= upgrade.getOutputMod();
            }
        }
        return mod;
    }
}
