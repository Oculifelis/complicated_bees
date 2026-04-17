package com.accbdd.complicated_bees.network.packet;


import com.accbdd.complicated_bees.block.entity.BeeSorterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record UpdateSorterServerbound(BlockPos pos, byte[] newFilters, List<String> speciesFilters) implements CustomPacketPayload {
    public static final Type<UpdateSorterServerbound> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "update_sorter_serverbound"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSorterServerbound> STREAM_CODEC = StreamCodec.of(UpdateSorterServerbound::encode, UpdateSorterServerbound::decode);

    private static void encode(RegistryFriendlyByteBuf buf, UpdateSorterServerbound payload) {
        buf.writeBlockPos(payload.pos);
        buf.writeByteArray(payload.newFilters);
        buf.writeCollection(payload.speciesFilters, FriendlyByteBuf::writeUtf);
    }

    private static UpdateSorterServerbound decode(RegistryFriendlyByteBuf buffer) {
        return new UpdateSorterServerbound(buffer.readBlockPos(), buffer.readByteArray(), buffer.readCollection(ArrayList::new, FriendlyByteBuf::readUtf));
    }

    public static void handle(UpdateSorterServerbound packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            if (sender.level().getBlockEntity(packet.pos) instanceof BeeSorterBlockEntity sorter) {
                sorter.setTypeFilters(packet.newFilters);
                sorter.setSpeciesFilters(packet.speciesFilters.toArray(String[]::new));
                sender.level().sendBlockUpdated(packet.pos, sorter.getBlockState(), sorter.getBlockState(), 3);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
