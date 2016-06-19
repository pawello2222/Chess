package com.pawello2222.chess.service;

import com.pawello2222.chess.model.Spot;

/**
 * Piece move validator interface.
 *
 * @author Pawel Wiszenko
 */
public interface IMoveValidator
{
    void validateMovesForSpot( Spot spot );

    int countMovesForSpot( Spot spot );

    void updateCheckFlag();
}
