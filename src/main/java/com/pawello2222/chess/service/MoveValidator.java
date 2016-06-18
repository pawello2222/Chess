package com.pawello2222.chess.service;

import com.pawello2222.chess.model.*;

/**
 * Piece move validator class.
 *
 * @author Pawel Wiszenko
 */
public class MoveValidator implements IMoveValidator
{
    private Spot[][] spots;

    private Piece sourcePiece;

    public MoveValidator( Spot[][] spots )
    {
        this.spots = spots;
    }

    @Override
    public void validateMovesForSpot( Spot spot )
    {
        sourcePiece = spot.getPiece();
        Spot nextSpot;

        switch ( sourcePiece.getType() )
        {
            case PAWN:
                nextSpot = getNextSpot( spot, Side.N );
                updateBasicFlags( nextSpot, true );

                if ( nextSpot != null && nextSpot.getPiece() == null && sourcePiece.isUnmoved() )
                {
                    nextSpot = getNextSpot( nextSpot, Side.N );
                    updateBasicFlags( nextSpot, true );
                }

                nextSpot = getNextSpot( spot, Side.NW );
                updateBasicFlags( nextSpot, false );

                nextSpot = getNextSpot( spot, Side.NE );
                updateBasicFlags( nextSpot, false );

                nextSpot = getNextSpot( spot, Side.NE );
                updateBasicFlags( nextSpot, false );

                nextSpot = getNextSpot( spot, Side.W );
                updateEnPassantFlag( nextSpot );

                nextSpot = getNextSpot( spot, Side.E );
                updateEnPassantFlag( nextSpot );
                break;

            case ROOK:
                break;

            case KNIGHT:
                break;

            case BISHOP:
                break;

            case QUEEN:
                break;

            case KING:
                break;
        }
    }

    private void updateBasicFlags( Spot spot, boolean validWhenFree )
    {
        if ( spot == null )
            return;

        if ( validWhenFree && spot.getPiece() == null )
            spot.setValidMoveFlag( true );
        else if ( !validWhenFree && spot.getPiece() != null && spot.getPiece().getColor() != sourcePiece.getColor() )
            spot.setValidMoveFlag( true );
    }

    private void updateEnPassantFlag( Spot spot )
    {
        if ( spot == null )
            return;

        Piece targetPiece = spot.getPiece();
        if ( targetPiece != null && targetPiece.getColor() != sourcePiece.getColor() && spot.isEnPassantFlag() )
            getNextSpot( spot, Side.N ).setValidMoveFlag( true );
    }

    private Spot getNextSpot( Spot spot, Side side )
    {
        if ( spot == null )
            return null;

        int diff = sourcePiece.getColor() == PieceColor.WHITE ? -1 : 1;

        switch( side )
        {
            case N:
                if ( spot.getRow() + diff < 0 || spot.getRow() + diff > 7 )
                    break;
                return spots[ spot.getColumn() ][ spot.getRow() + diff ];

            case E:
                if ( spot.getColumn() - diff < 0 || spot.getColumn() - diff > 7 )
                    break;
                return spots[ spot.getColumn() - diff ][ spot.getRow() ];

            case S:
                if ( spot.getRow() - diff < 0 || spot.getRow() - diff > 7 )
                    break;
                return spots[ spot.getColumn() ][ spot.getRow() - diff ];

            case W:
                if ( spot.getColumn() + diff < 0 || spot.getColumn() + diff > 7 )
                    break;
                return spots[ spot.getColumn() + diff ][ spot.getRow() ];

            case NE:
                return getNextSpot( getNextSpot( spot, Side.N ), Side.E );

            case SE:
                return getNextSpot( getNextSpot( spot, Side.S ), Side.E );

            case SW:
                return getNextSpot( getNextSpot( spot, Side.S ), Side.W );

            case NW:
                return getNextSpot( getNextSpot( spot, Side.N ), Side.W );
        }

        return null;
    }
}
