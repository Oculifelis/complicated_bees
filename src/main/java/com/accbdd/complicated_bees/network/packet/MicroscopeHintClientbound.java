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

public record MicroscopeHintClientbound(byte index, byte hint) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MicroscopeHintClientbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "microscope_hint_clientbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MicroscopeHintClientbound> STREAM_CODEC = StreamCodec.of(MicroscopeHintClientbound::encode, MicroscopeHintClientbound::decode);

    private static void encode(FriendlyByteBuf buf, MicroscopeHintClientbound payload) {
        buf.writeByte(payload.index);
        buf.writeByte(payload.hint);
    }

    private static MicroscopeHintClientbound decode(FriendlyByteBuf buf) {
        return new MicroscopeHintClientbound(buf.readByte(), buf.readByte());
    }

    public static void handle(MicroscopeHintClientbound packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLLoader.getDist().isClient()) MicroscopeHintClientbound.handlePacket(packet, ctx);
        });
    }

    public static void handlePacket(MicroscopeHintClientbound packet, IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof MicroscopeScreen screen) {
            //ComplicatedBees.LOGGER.debug("got packet with hint {} for index {}", packet.hint, packet.index);
            screen.getGame().hint(packet.index, packet.hint);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
