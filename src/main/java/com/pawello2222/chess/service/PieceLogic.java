package com.pawello2222.chess.service;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

import java.util.List;

/**
 * Piece logic class.
 *
 * @author Pawel Wiszenko
 */
class PieceLogic
{
    static void removePiece( List< Piece > pieces, Spot[][] spots, Spot source, Spot target )
    {
        if ( source.getPiece().getType() == PieceType.PAWN
             && target.getColumn() != source.getColumn()
             && target.getPiece() == null )
        {
            pieces.remove( spots[ target.getColumn() ][ source.getRow() ].getPiece() );
            spots[ target.getColumn() ][ source.getRow() ].setPiece( null );
        }
        else if ( target.getPiece() != null )
            pieces.remove( target.getPiece() );
    }

    static void movePiece( Spot source, Spot target )
    {
        Piece sourcePiece = source.getPiece();

        if ( sourcePiece.isUnmoved() )
            sourcePiece.setUnmoved( false );

        if ( sourcePiece.getType() == PieceType.PAWN && Math.abs( target.getRow() - source.getRow() ) == 2 )
            target.setEnPassantFlag( true );

        target.setPiece( source.getPiece() );
        target.getPiece().setCoordinatesToSpot( target );
        source.setPiece( null );
    }

    static PieceColor getOppositePieceColor( PieceColor pieceColor )
    {
        return pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    static boolean isPieceAtSpot( Spot spot, PieceType type, PieceColor color )
    {
        return spot != null
               && spot.getPiece() != null
               && spot.getPiece().getType() == type
               && spot.getPiece().getColor() == color;
    }
}
