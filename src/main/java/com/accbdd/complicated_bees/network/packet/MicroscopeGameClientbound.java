package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MicroscopeGameClientbound(GameState state) implements IModPacket {
    public enum GameState {
        WON,
        ONGOING,
        FAILED,
        START,
        CLEAR
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(state());
    }

    public static MicroscopeGameClientbound decode(FriendlyByteBuf buf) {
        return new MicroscopeGameClientbound(buf.readEnum(GameState.class));
    }

    public static void handle(MicroscopeGameClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MicroscopeGameClientbound.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handlePacket(MicroscopeGameClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        if (Minecraft.getInstance().screen instanceof MicroscopeScreen screen) {
            //ComplicatedBees.LOGGER.debug("got packet with state {}", packet.state);
            GameState state = packet.state();
            switch (state) {
                case CLEAR:
                    screen.clearGame();
                    break;
                case START:
                    screen.startGame();
                default:
                    screen.getGame().setGameState(state);
                    break;
            }
        }
    }
}
