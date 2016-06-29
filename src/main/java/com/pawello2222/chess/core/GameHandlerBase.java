package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.net.NetworkReceiver;
import com.pawello2222.chess.net.NetworkSender;

/**
 * Game handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class GameHandlerBase implements GraphicsHandler, MoveHandler, NetworkReceiver
{
    @Override
    public abstract void updateGraphics();

    @Override
    public abstract void setFocusOn( Piece piece );

    @Override
    public abstract void updatePossibleMoves( Spot spot );

    @Override
    public abstract void movePiece( Spot sourceSpot, Spot targetSpot, boolean isOwnMove );

    @Override
    public abstract void receive( String data );

    @Override
    public abstract void setNetworkSender( NetworkSender networkSender );

    public abstract void setGame( GameBase game );
}
