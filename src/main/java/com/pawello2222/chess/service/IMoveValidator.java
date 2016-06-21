package com.pawello2222.chess.service;

import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

/**
 * Piece move validator interface.
 *
 * @author Pawel Wiszenko
 */
public interface IMoveValidator
{
    void updateValidMoveFlags( Spot spot );

    void updateLastMoveFlags( Spot source, Spot target );

    void updateCheckFlag();

    void updateSpecialMoveFlag( Spot source, Spot target );
}
