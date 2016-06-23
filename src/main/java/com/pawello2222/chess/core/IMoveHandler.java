package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Spot;

/**
 * Move handler interface.
 *
 * @author Pawel Wiszenko
 */
interface IMoveHandler
{
    void updatePossibleMoves( Spot spot );

    void movePiece( Spot sourceSpot, Spot targetSpot );
}
