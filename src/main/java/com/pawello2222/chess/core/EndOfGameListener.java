package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;

/**
 * End of game listener.
 *
 * @author Pawel Wiszenko
 */
interface EndOfGameListener
{
    void endOfGame( GameState gameState );
}
