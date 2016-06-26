package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;

/**
 * Graphics handler interface.
 *
 * @author Pawel Wiszenko
 */
interface GraphicsHandler
{
    void updateGraphics();

    void setFocusOn( Piece piece );
}
