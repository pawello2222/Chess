package com.pawello2222.chess.service;

import com.pawello2222.chess.core.Board;
import com.pawello2222.chess.model.Spot;

/**
 * Piece move validator.
 *
 * @author Pawel Wiszenko
 */
public class MoveValidator implements IMoveValidator
{
    private Board board;

    private Spot[][] spots;

    public MoveValidator( Board board )
    {
        this.board = board;
        this.spots = board.getSpots();
    }

    @Override
    public void validateMovesForSpot( Spot spot )
    {
        spot.setValidMoveFlg( true );
    }
}
