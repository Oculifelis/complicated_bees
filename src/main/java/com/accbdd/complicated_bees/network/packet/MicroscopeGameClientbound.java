package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record MicroscopeGameClientbound(GameState state) implements CustomPacketPayload {
    public enum GameState {
        WON,
        ONGOING,
        FAILED,
        START,
        CLEAR
    }

    public static final Type<MicroscopeGameClientbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "microscope_game_clientbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MicroscopeGameClientbound> STREAM_CODEC = StreamCodec.of(MicroscopeGameClientbound::encode, MicroscopeGameClientbound::decode);

    public static void encode(RegistryFriendlyByteBuf buf, MicroscopeGameClientbound payload) {
        buf.writeEnum(payload.state);
    }

    public static MicroscopeGameClientbound decode(FriendlyByteBuf buf) {
        return new MicroscopeGameClientbound(buf.readEnum(GameState.class));
    }

    public static void handle(MicroscopeGameClientbound packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLLoader.getDist().isClient()) MicroscopeGameClientbound.handlePacket(packet, ctx);
        });
    }

    public static void handlePacket(MicroscopeGameClientbound packet, IPayloadContext ctx) {
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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
