package com.accbdd.complicated_bees.bees.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Random;

public class JazzyEffect extends BeeEffect {
    private static final float[] pentatonicFrequencies = {
            // Octave 1
            0.529732f,  // G
            0.594604f,  // A
            0.707107f,  // C
            0.793701f,  // D
            0.890899f,  // E

            // Octave 2
            1.059463f,  // G
            1.189207f,  // A
            1.414214f,  // C
            1.587401f,  // D
            1.781797f   // E
    };
    private static final Random rand = new Random();

    @Override
    public void runEffect(BlockEntity apiary, ItemStack queen, int cycleProgress) {
        if (cycleProgress % 2 == 0 && rand.nextFloat() < 0.5f) {
            BlockPos pos = apiary.getBlockPos();
            var note = pentatonicFrequencies[rand.nextInt(10)];
            apiary.getLevel().playSound(null, pos, SoundEvents.NOTE_BLOCK_HARP.get(), SoundSource.BLOCKS, 1, note);
            if (apiary.getLevel() instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(ParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, 1, (double)note / 24.0D, 0.0D, 0.0D, 1);
        }
    }
}
