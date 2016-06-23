package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;

/**
 * Board handler base class.
 *
 * @author Pawel Wiszenko
 */
abstract class BoardHandlerBase implements IGraphicsHandler, IMoveHandler
{
    @Override
    public abstract void updateGraphics();

    @Override
    public abstract void setFocusOn( Piece piece );

    @Override
    public abstract void updatePossibleMoves( Spot spot );

    @Override
    public abstract void movePiece( Spot sourceSpot, Spot targetSpot );
}
