package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.WireGamePacketClientbound;

public interface IMicroscopeGame {
    void sendGuess(byte[] guess);

    void setGameState(WireGamePacketClientbound.GameState state);

    void reset();
}
