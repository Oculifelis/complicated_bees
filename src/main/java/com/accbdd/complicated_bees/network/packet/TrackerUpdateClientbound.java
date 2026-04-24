package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record TrackerUpdateClientbound(UpdateType updateType, ResourceLocation loc) implements CustomPacketPayload {
    public enum UpdateType {
        SPECIES,
        MUTATION,
        RESEARCH
    }

    public static final Type<TrackerUpdateClientbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "tracker_update_clientbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TrackerUpdateClientbound> STREAM_CODEC = StreamCodec.of(TrackerUpdateClientbound::encode, TrackerUpdateClientbound::decode);

    private static void encode(RegistryFriendlyByteBuf buf, TrackerUpdateClientbound payload) {
        buf.writeEnum(payload.updateType);
        buf.writeResourceLocation(payload.loc);
    }

    private static TrackerUpdateClientbound decode(RegistryFriendlyByteBuf buf) {
        return new TrackerUpdateClientbound(buf.readEnum(UpdateType.class), buf.readResourceLocation());
    }

    public static void handle(TrackerUpdateClientbound packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (FMLLoader.getDist().isClient()) TrackerUpdateClientbound.handlePacket(packet, ctx);
        });
    }

    public static void handlePacket(TrackerUpdateClientbound packet, IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null)
            throw new IllegalStateException("received update packet on side with null player");
        if (BreedingTracker.CLIENT_INSTANCE == null)
            BreedingTracker.CLIENT_INSTANCE = new BreedingTracker(Minecraft.getInstance().player.getUUID());
        BreedingTracker.updateFromPacket(packet);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
