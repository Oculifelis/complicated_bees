package com.accbdd.complicated_bees.network.packet;


import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.screen.MicroscopeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

public record WireGamePacketServerbound(byte[] guesses) implements IModPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(guesses);
    }

    public static WireGamePacketServerbound decode(FriendlyByteBuf buffer) {
        return new WireGamePacketServerbound(buffer.readByteArray());
    }

    public static void handle(WireGamePacketServerbound packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if (sender.containerMenu instanceof MicroscopeMenu microscopeMenu) {
                Random rand = new Random();
                BlockPos pos = microscopeMenu.getPos();
                ServerLevel level = (ServerLevel) sender.level();
                if (!level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
                    return;
                byte[] researchCode = microscopeMenu.getResearchCode();
                ComplicatedBees.LOGGER.debug("we are in a microscope! guesses from client: {}, mutation code: {}", packet.guesses, researchCode);
                if (Arrays.equals(packet.guesses, researchCode)) {
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new WireGamePacketClientbound(WireGamePacketClientbound.GameState.WON));
                    //microscopeMenu.getSlot(0).set(Items.DIAMOND.getDefaultInstance());
                    level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
                    microscopeMenu.research();
                } else {
                    for (int i = 0; i < researchCode.length; i++) {
                        if (packet.guesses[i] != researchCode[i] && packet.guesses[i] != -1) {
                            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new WireGamePacketClientbound(WireGamePacketClientbound.GameState.FAILED));
                            level.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
                            return;
                        }
                    }
                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sender), new WireGamePacketClientbound(WireGamePacketClientbound.GameState.ONGOING));
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
