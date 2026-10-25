package com.accbdd.complicated_bees.bees.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ResurrectionEffect extends BeeEffect {
    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (apiary.getLevel() == null) return;

        if (cycleProgress == 0) {
            ServerLevel level = (ServerLevel) apiary.getLevel();
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Entity entity : getTerritoryEntities(apiary, queen)) {
                if (entity instanceof ItemEntity itemEntity) {
                    ItemStack stack = itemEntity.getItem();
                    if (stack.is(Items.BONE) ||
                            stack.is(Items.ROTTEN_FLESH) ||
                            stack.is(Items.GUNPOWDER) ||
                            stack.is(Items.PORKCHOP) ||
                            stack.is(Items.BEEF) ||
                            stack.is(Items.CHICKEN) ||
                            stack.is(Items.MUTTON) ||
                            stack.is(Items.SLIME_BALL) ||
                            stack.is(Items.RABBIT) ||
                            stack.is(Items.ENDER_PEARL) ||
                            stack.is(Items.BLAZE_ROD) ||
                            stack.is(Items.GHAST_TEAR))
                        itemEntities.add(itemEntity);
                }
            }
            if (!itemEntities.isEmpty()) {
                ItemEntity chosen = itemEntities.get(apiary.getLevel().getRandom().nextInt(itemEntities.size()));
                ItemStack chosenStack = chosen.getItem();
                if (chosenStack.is(Items.BONE)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("skeleton")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.ROTTEN_FLESH)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("zombie")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.GUNPOWDER)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("creeper")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.PORKCHOP)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("pig")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.BEEF)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("cow")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.CHICKEN)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("chicken")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.MUTTON)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("sheep")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.SLIME_BALL)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("slime")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.RABBIT)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("rabbit")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.ENDER_PEARL)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("enderman")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.BLAZE_ROD)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("blaze")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                } else if (chosenStack.is(Items.GHAST_TEAR)) {
                    net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("ghast")).spawn(level, chosen.blockPosition(), EntitySpawnReason.SPAWNER);
                }
                chosenStack.shrink(1);
            }
        }
    }
}
