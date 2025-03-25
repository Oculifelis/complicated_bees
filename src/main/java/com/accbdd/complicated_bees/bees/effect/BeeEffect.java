package com.accbdd.complicated_bees.bees.effect;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.IBeeHousing;
import com.accbdd.complicated_bees.bees.gene.GeneTerritory;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BeeEffect implements IBeeEffect {
    @Override
    public abstract void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress);

    /**
     * @param be the BlockEntity generating this effect
     * @param queen  the queen generating this effect
     * @return a list of all entities in this queen's territory, minus any players wearing a full set of apiarist armor
     */
    protected List<Entity> getTerritoryEntities(BlockEntity be, ItemStack queen) {
        List<Entity> entities = new ArrayList<>();
        Vec3 center = be.getBlockPos().getCenter();
        float rangeModifier = 1f;
        if (be instanceof IBeeHousing housing) {
            for (BeeHousingModifier modifier : housing.getHousingModifiers()) {
                rangeModifier *= modifier.getTerritoryMod();
            }
        }
        int[] radii = (int[]) GeneticHelper.getGeneValue(queen, GeneTerritory.ID, true);
        Vec3 offset = new Vec3(radii[0] * rangeModifier, radii[1] * rangeModifier, radii[0] * rangeModifier);
        for (Entity entity : Objects.requireNonNull(be.getLevel()).getEntities(null, new AABB(center.add(offset), center.subtract(offset)))) {
            if (entity instanceof Player player && hasApiaristArmorEquipped(player))
                continue;
            entities.add(entity);
        }
        return entities;
    }

    /**
     * @param be the BlockEntity generating this effect
     * @param queen  the queen generating this effect
     * @return a BlockPosBoxIterator sized to the queen's territory
     */
    protected BlockPosBoxIterator getBlockIterator(BlockEntity be, ItemStack queen) {
        float rangeModifier = 1f;
        if (be instanceof IBeeHousing housing) {
            for (BeeHousingModifier modifier : housing.getHousingModifiers()) {
                rangeModifier *= modifier.getTerritoryMod();
            }
        }
        int[] radii = (int[]) GeneticHelper.getGeneValue(queen, GeneTerritory.ID, true);
        return new BlockPosBoxIterator(be.getBlockPos(), Math.round(radii[0] * rangeModifier), Math.round(radii[1] * rangeModifier));
    }

    private boolean hasApiaristArmorEquipped(Player player) {
        Inventory inv = player.getInventory();
        return inv.getArmor(0).is(ItemsRegistration.APIARIST_BOOTS.get()) &&
                inv.getArmor(1).is(ItemsRegistration.APIARIST_LEGGINGS.get()) &&
                inv.getArmor(2).is(ItemsRegistration.APIARIST_CHESTPLATE.get()) &&
                inv.getArmor(3).is(ItemsRegistration.APIARIST_HELMET.get());
    }
}
