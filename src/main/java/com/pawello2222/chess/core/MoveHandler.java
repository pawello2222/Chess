package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Spot;

/**
 * Move handler interface.
 *
 * @author Pawel Wiszenko
 */
interface MoveHandler
{
    void updatePossibleMoves( Spot spot );

    void movePiece( Spot sourceSpot, Spot targetSpot, char promotion, boolean isOwnMove );
}
