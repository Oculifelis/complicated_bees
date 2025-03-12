package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.MicroscopeGameClientbound;

/**
 * An interface for games for the microscope - these games have four states (see {@link MicroscopeGameClientbound.GameState}), which determine how the game will proceed.
 *
 */
public interface IMicroscopeGame {
    /**
     * Sends a guess against a sequence held by the server - will receive a packet that calls {@link #setGameState(MicroscopeGameClientbound.GameState)}.
     * @param guess the sequence of guesses to check against the server
     */
    void sendGuess(byte[] guess);

    /**
     * Sets the game's state - called by a received packet
     * @param state the state to set the game to
     */
    void setGameState(MicroscopeGameClientbound.GameState state);

    /**
     * generally called by a packet, provides a hint for the sequence at index
     * @param index - the index in the sequence to provide a hint for
     * @param hint - the correct number for index
     */
    void hint(byte index, byte hint);

    /**
     * resets the game
     */
    void reset();
}
