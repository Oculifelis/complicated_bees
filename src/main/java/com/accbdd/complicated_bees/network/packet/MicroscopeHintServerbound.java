package com.accbdd.complicated_bees.network.packet;


import com.accbdd.complicated_bees.screen.MicroscopeMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public record MicroscopeHintServerbound() implements IModPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {

    }

    public static MicroscopeHintServerbound decode(FriendlyByteBuf buffer) {
        return new MicroscopeHintServerbound();
    }

    public static void handle(MicroscopeHintServerbound packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if (sender.containerMenu instanceof MicroscopeMenu microscopeMenu) {
                microscopeMenu.trySendHint();
            }
        });
        context.get().setPacketHandled(true);
    }
}
