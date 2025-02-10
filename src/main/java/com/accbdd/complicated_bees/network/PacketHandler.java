package com.accbdd.complicated_bees.network;

import com.accbdd.complicated_bees.network.packet.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class PacketHandler {
    private static final String VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void register() {
        PacketRegistry registry = new PacketRegistry(CHANNEL);

        //surely there is a better way to do this
        registry.register(MicroscopeGameServerbound.class, MicroscopeGameServerbound::decode, MicroscopeGameServerbound::handle);
        registry.register(MicroscopeGameClientbound.class, MicroscopeGameClientbound::decode, MicroscopeGameClientbound::handle);
        registry.register(MicroscopeHintServerbound.class, MicroscopeHintServerbound::decode, MicroscopeHintServerbound::handle);
        registry.register(MicroscopeHintClientbound.class, MicroscopeHintClientbound::decode, MicroscopeHintClientbound::handle);
        registry.register(TrackerSyncClientbound.class, TrackerSyncClientbound::decode, TrackerSyncClientbound::handle);
        registry.register(TrackerUpdateClientbound.class, TrackerUpdateClientbound::decode, TrackerUpdateClientbound::handle);
    }

    private static final class PacketRegistry {
        private final SimpleChannel channel;
        private int packetId;

        private PacketRegistry(SimpleChannel channel) {
            this.channel = channel;
        }

        public <P extends IModPacket> void register(Class<P> packetClass, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, Supplier<NetworkEvent.Context>> context) {
            channel.registerMessage(packetId++, packetClass, P::encode, decoder, context);
        }
    }
}
