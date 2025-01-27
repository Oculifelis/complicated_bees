package com.accbdd.complicated_bees.network.packet;

import com.accbdd.complicated_bees.genetics.tracking.BreedingTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TrackerUpdateClientbound(UpdateType type, ResourceLocation loc) implements IModPacket {
    public enum UpdateType {
        SPECIES,
        MUTATION,
        RESEARCH
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeResourceLocation(loc);
    }

    public static TrackerUpdateClientbound decode(FriendlyByteBuf buf) {
        return new TrackerUpdateClientbound(buf.readEnum(UpdateType.class), buf.readResourceLocation());
    }

    public static void handle(TrackerUpdateClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TrackerUpdateClientbound.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void handlePacket(TrackerUpdateClientbound packet, Supplier<NetworkEvent.Context> ctx) {
        if (Minecraft.getInstance().player == null)
            throw new IllegalStateException("received update packet on side with null player");
        if (BreedingTracker.CLIENT_INSTANCE == null)
            BreedingTracker.CLIENT_INSTANCE = new BreedingTracker(Minecraft.getInstance().player.getUUID());

        BreedingTracker.updateFromPacket(packet);
    }
}
