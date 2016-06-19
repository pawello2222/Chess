package com.pawello2222.chess.service;

import com.pawello2222.chess.model.Spot;

/**
 * Piece move validator interface.
 *
 * @author Pawel Wiszenko
 */
public interface IMoveValidator
{
    void updateFlagsForSpot( Spot spot );

    void updateLastMoveFlag( Spot source, Spot target );

    void updateCheckFlag();

    void updateEnPassantFlag( Spot source, Spot target );
}
