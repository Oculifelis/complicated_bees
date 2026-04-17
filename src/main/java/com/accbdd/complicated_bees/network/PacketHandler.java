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
        registrar.playToServer(MicroscopeHintServerbound.TYPE, MicroscopeHintServerbound.STREAM_CODEC, MicroscopeHintServerbound::handle);
        registrar.playToClient(MicroscopeHintClientbound.TYPE, MicroscopeHintClientbound.STREAM_CODEC, MicroscopeHintClientbound::handle);
        registrar.playToClient(TrackerSyncClientbound.TYPE, TrackerSyncClientbound.STREAM_CODEC, TrackerSyncClientbound::handle);
        registrar.playToClient(TrackerUpdateClientbound.TYPE, TrackerUpdateClientbound.STREAM_CODEC, TrackerUpdateClientbound::handle);
        registrar.playToServer(UpdateSorterServerbound.TYPE, UpdateSorterServerbound.STREAM_CODEC, UpdateSorterServerbound::handle);
    }
}
