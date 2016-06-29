package com.pawello2222.chess.model;

/**
 * Piece type.
 *
 * @author Pawel Wiszenko
 */
public enum PieceType
{
    PAWN( "PAWN" ),
    ROOK( "ROOK" ),
    KNIGHT( "KNIGHT" ),
    BISHOP( "BISHOP" ),
    QUEEN( "QUEEN" ),
    KING( "KING" );

    private final String text;

    PieceType( final String text )
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return text;
    }
}
