package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record TrackerSyncClientbound(BreedingTracker tracker) implements CustomPacketPayload {
    public static final Type<TrackerSyncClientbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "tracker_sync_clientbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TrackerSyncClientbound> STREAM_CODEC = StreamCodec.of(TrackerSyncClientbound::encode, TrackerSyncClientbound::decode);

    private static void encode(RegistryFriendlyByteBuf buf, TrackerSyncClientbound payload) {
        CompoundTag tag = new CompoundTag();
        buf.writeNbt(payload.tracker.save(tag, buf.registryAccess()));
    }

    private static TrackerSyncClientbound decode(RegistryFriendlyByteBuf buf) {
        CompoundTag data = buf.readNbt();
        if (data != null)
            return new TrackerSyncClientbound(BreedingTracker.load(data, buf.registryAccess()));
        else
            return new TrackerSyncClientbound(null);
    }

    public static void handle(TrackerSyncClientbound packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLLoader.getDist().isClient()) TrackerSyncClientbound.handlePacket(packet, ctx);
        });
    }

    public static void handlePacket(TrackerSyncClientbound packet, IPayloadContext ctx) {
        BreedingTracker.CLIENT_INSTANCE = packet.tracker;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
