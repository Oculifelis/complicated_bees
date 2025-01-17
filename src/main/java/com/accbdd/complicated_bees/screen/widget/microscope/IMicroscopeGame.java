package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.WireGamePacketClientbound;

/**
 * An interface for games for the microscope - these games have four states (see {@link com.accbdd.complicated_bees.network.packet.WireGamePacketClientbound.GameState}), which determine how the game will proceed.
 *
 */
public interface IMicroscopeGame {
    /**
     * Sends a guess against a sequence held by the server - will receive a packet that calls {@link #setGameState(WireGamePacketClientbound.GameState)}.
     * @param guess the sequence of guesses to check against the server
     */
    void sendGuess(byte[] guess);

    /**
     * Sets the game's state - called by a received packet
     * @param state the state to set the game to
     */
    void setGameState(WireGamePacketClientbound.GameState state);

    /**
     * resets the game
     */
    void reset();
}
