package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record MicroscopeHintServerbound() implements CustomPacketPayload {
    public static final Type<MicroscopeHintServerbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "microscope_hint_serverbound"));
    public static final MicroscopeHintServerbound INSTANCE = new MicroscopeHintServerbound();
    public static final StreamCodec<RegistryFriendlyByteBuf, MicroscopeHintServerbound> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static void handle(MicroscopeHintServerbound packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            if (sender.containerMenu instanceof MicroscopeMenu microscopeMenu) {
                microscopeMenu.trySendHint();
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
