package com.accbdd.complicated_bees.network;

import com.accbdd.complicated_bees.network.packet.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);

        //surely there is a better way to do this
        registrar.playToServer(MicroscopeGameServerbound.TYPE, MicroscopeGameServerbound.STREAM_CODEC, MicroscopeGameServerbound::handle);
        registrar.playToClient(MicroscopeGameClientbound.TYPE, MicroscopeGameClientbound.STREAM_CODEC, MicroscopeGameClientbound::handle);
        registrar.playToServer(MicroscopeHintServerbound.class, MicroscopeHintServerbound::decode, MicroscopeHintServerbound::handle);
        registrar.playToClient(MicroscopeHintClientbound.class, MicroscopeHintClientbound::decode, MicroscopeHintClientbound::handle);
        registrar.playToClient(TrackerSyncClientbound.class, TrackerSyncClientbound::decode, TrackerSyncClientbound::handle);
        registrar.playToClient(TrackerUpdateClientbound.class, TrackerUpdateClientbound::decode, TrackerUpdateClientbound::handle);
        registrar.playToServer(UpdateSorterServerbound.class, UpdateSorterServerbound::decode, UpdateSorterServerbound::handle);
    }
}
