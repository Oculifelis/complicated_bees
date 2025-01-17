package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record WireGamePacketClientbound(GameState state) implements IModPacket {
    public enum GameState {
        WON,
        ONGOING,
        FAILED,
        START
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(state());
    }

    public static WireGamePacketClientbound decode(FriendlyByteBuf buf) {
        return new WireGamePacketClientbound(buf.readEnum(GameState.class));
    }

    public static void handle(WireGamePacketClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WireGamePacketClientbound.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handlePacket(WireGamePacketClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        if (Minecraft.getInstance().screen instanceof MicroscopeScreen screen) {
            GameState state = packet.state();
            screen.getGame().setGameState(state);
            switch (state) {
                case START:
                    screen.getGame().reset();
            }
        }
    }
}
