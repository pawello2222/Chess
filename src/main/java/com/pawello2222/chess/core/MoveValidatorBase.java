package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Spot;

/**
 * Move validator base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class MoveValidatorBase
{
    public abstract void updateFlagsAfterMove( Spot source, Spot target );

    public abstract void updateValidMoveFlags( Spot spot );

    public abstract int getPossibleMovesCount();
}
