package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Spot;

/**
 * Move validator interface.
 *
 * @author Pawel Wiszenko
 */
interface IMoveValidator
{
    void updateValidMoveFlags( Spot spot );

    void updateFlagsAfterMove( Spot source, Spot target );

    int getPossibleMovesCount();
}
