package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.net.NetworkReceiver;

/**
 * Board handler base class.
 *
 * @author Pawel Wiszenko
 */
abstract class BoardHandlerBase implements GraphicsHandler, MoveHandler, NetworkReceiver
{
    @Override
    public abstract void updateGraphics();

    @Override
    public abstract void setFocusOn( Piece piece );

    @Override
    public abstract void updatePossibleMoves( Spot spot );

    @Override
    public abstract void movePiece( Spot sourceSpot, Spot targetSpot );

    @Override
    public abstract void receiveMove( Spot sourceSpot, Spot targetSpot );
}
