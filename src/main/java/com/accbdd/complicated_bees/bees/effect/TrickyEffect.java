package com.accbdd.complicated_bees.bees.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Random;

public class TrickyEffect extends BeeEffect {
    public static Random rand = new Random();
    public static List<SoundEvent> sounds = List.of(SoundEvents.CREEPER_PRIMED, SoundEvents.GRAVEL_STEP, SoundEvents.SKELETON_AMBIENT, SoundEvents.AMBIENT_CAVE.get());

    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (cycleProgress % 20 == 0 & rand.nextFloat() < 0.2f) {
            BlockPos pos = apiary.getBlockPos();
            apiary.getLevel().playSound(null, pos, sounds.get(rand.nextInt(sounds.size())), SoundSource.BLOCKS, 1, 1);
        }
    }
}
