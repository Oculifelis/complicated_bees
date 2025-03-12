package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MicroscopeHintClientbound(byte index, byte hint) implements IModPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(index);
        buf.writeByte(hint);
    }

    public static MicroscopeHintClientbound decode(FriendlyByteBuf buf) {
        return new MicroscopeHintClientbound(buf.readByte(), buf.readByte());
    }

    public static void handle(MicroscopeHintClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MicroscopeHintClientbound.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handlePacket(MicroscopeHintClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        if (Minecraft.getInstance().screen instanceof MicroscopeScreen screen) {
            //ComplicatedBees.LOGGER.debug("got packet with hint {} for index {}", packet.hint, packet.index);
            screen.getGame().hint(packet.index, packet.hint);
        }
    }
}
