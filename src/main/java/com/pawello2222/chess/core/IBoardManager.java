package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Spot;

/**
 * Board manager interface.
 *
 * @author Pawel Wiszenko
 */
interface IBoardManager
{
    void updateMovements( Spot spot );

    void movePiece( Spot sourceSpot, Spot targetSpot );

    void nextTurn();
}
