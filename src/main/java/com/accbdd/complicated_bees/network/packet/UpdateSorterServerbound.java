package com.accbdd.complicated_bees.network.packet;


import com.accbdd.complicated_bees.block.entity.BeeSorterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record UpdateSorterServerbound(BlockPos pos, byte[] newFilters, List<String> speciesFilters) implements IModPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeByteArray(newFilters);
        buf.writeCollection(speciesFilters, FriendlyByteBuf::writeUtf);
    }

    public static UpdateSorterServerbound decode(FriendlyByteBuf buffer) {
        return new UpdateSorterServerbound(buffer.readBlockPos(), buffer.readByteArray(), buffer.readCollection(ArrayList::new, FriendlyByteBuf::readUtf));
    }

    public static void handle(UpdateSorterServerbound packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if (sender.level().getBlockEntity(packet.pos) instanceof BeeSorterBlockEntity sorter) {
                sorter.setTypeFilters(packet.newFilters);
                sorter.setSpeciesFilters(packet.speciesFilters.toArray(String[]::new));
                sender.level().sendBlockUpdated(packet.pos, sorter.getBlockState(), sorter.getBlockState(), 3);
            }
        });
        context.get().setPacketHandled(true);
    }
}
