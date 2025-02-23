package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TrackerSyncClientbound(BreedingTracker tracker) implements IModPacket {

    @Override
    public void encode(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        buf.writeNbt(tracker.save(tag));
    }

    public static TrackerSyncClientbound decode(FriendlyByteBuf buf) {
        CompoundTag data = buf.readNbt();
        if (data != null)
            return new TrackerSyncClientbound(BreedingTracker.load(data));
        else
            return new TrackerSyncClientbound(null);
    }

    public static void handle(TrackerSyncClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TrackerSyncClientbound.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handlePacket(TrackerSyncClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        BreedingTracker.CLIENT_INSTANCE = packet.tracker;
    }
}
