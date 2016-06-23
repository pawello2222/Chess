package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;

/**
 * Graphics handler interface.
 *
 * @author Pawel Wiszenko
 */
public interface IGraphicsHandler
{
    void updateGraphics();

    void setFocusOnPiece( Piece piece );
}
