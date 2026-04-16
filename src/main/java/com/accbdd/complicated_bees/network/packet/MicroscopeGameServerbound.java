package com.accbdd.complicated_bees.network.packet;


import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.screen.MicroscopeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Arrays;
import java.util.function.Supplier;

public record MicroscopeGameServerbound(byte[] guesses) implements IModPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(guesses);
    }

    public static MicroscopeGameServerbound decode(FriendlyByteBuf buffer) {
        return new MicroscopeGameServerbound(buffer.readByteArray());
    }

    public static void handle(MicroscopeGameServerbound packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if (sender.containerMenu instanceof MicroscopeMenu microscopeMenu) {
                BlockPos pos = microscopeMenu.getPos();
                ServerLevel level = (ServerLevel) sender.level();
                if (!level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
                    return;
                byte[] researchCode = microscopeMenu.getResearchCode();
                //ComplicatedBees.LOGGER.debug("we are in a microscope! guesses from client: {}, mutation code: {}", packet.guesses, researchCode);
                if (Arrays.equals(packet.guesses, researchCode)) {
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.WON));
                    microscopeMenu.setState(MicroscopeGameClientbound.GameState.WON);
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
                    level.playSound(sender, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS);
                    microscopeMenu.research();
                } else {
                    if (packet.guesses.length != researchCode.length) {
                        throw new IllegalStateException("recieved a packet of guesses with a different length than code!");
                    }
                    for (int i = 0; i < researchCode.length; i++) {
                        if (packet.guesses[i] != researchCode[i] && packet.guesses[i] != -1) {
                            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.FAILED));
                            microscopeMenu.setState(MicroscopeGameClientbound.GameState.FAILED);
                            microscopeMenu.shuffle();
                            level.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
                            return;
                        }
                    }
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.ONGOING));
                    microscopeMenu.setState(MicroscopeGameClientbound.GameState.ONGOING);
                    microscopeMenu.setGuess(packet.guesses.clone());
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
