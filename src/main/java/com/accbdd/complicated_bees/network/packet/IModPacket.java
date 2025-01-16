package com.accbdd.complicated_bees.network.packet;

import net.minecraft.network.FriendlyByteBuf;

public interface IModPacket {
    void encode(FriendlyByteBuf buf);
}
