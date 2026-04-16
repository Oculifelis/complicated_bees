package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Arrays;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record MicroscopeGameServerbound(byte[] guesses) implements CustomPacketPayload {
    public static final Type<MicroscopeGameServerbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "microscope_game_serverbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MicroscopeGameServerbound> STREAM_CODEC = StreamCodec.of(MicroscopeGameServerbound::encode, MicroscopeGameServerbound::decode);

    public static void encode(RegistryFriendlyByteBuf buf, MicroscopeGameServerbound payload) {
        buf.writeByteArray(payload.guesses);
    }

    public static MicroscopeGameServerbound decode(RegistryFriendlyByteBuf buffer) {
        return new MicroscopeGameServerbound(buffer.readByteArray());
    }

    public static void handle(MicroscopeGameServerbound packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            if (sender.containerMenu instanceof MicroscopeMenu microscopeMenu) {
                BlockPos pos = microscopeMenu.getPos();
                ServerLevel level = (ServerLevel) sender.level();
                if (!level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
                    return;
                byte[] researchCode = microscopeMenu.getResearchCode();
                //ComplicatedBees.LOGGER.debug("we are in a microscope! guesses from client: {}, mutation code: {}", packet.guesses, researchCode);
                if (Arrays.equals(packet.guesses, researchCode)) {
                    PacketDistributor.sendToPlayer(sender, new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.WON));
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
                            PacketDistributor.sendToPlayer(sender, new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.FAILED));
                            microscopeMenu.setState(MicroscopeGameClientbound.GameState.FAILED);
                            microscopeMenu.shuffle();
                            level.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 1);
                            return;
                        }
                    }
                    PacketDistributor.sendToPlayer(sender, new MicroscopeGameClientbound(MicroscopeGameClientbound.GameState.ONGOING));
                    microscopeMenu.setState(MicroscopeGameClientbound.GameState.ONGOING);
                    microscopeMenu.setGuess(packet.guesses.clone());
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
