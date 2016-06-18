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
        Spot nextSpot;
        sourcePiece = spot.getPiece();

        if ( sourcePiece.getType() == PieceType.PAWN )
        {
            nextSpot = getNextSpot( spot, Side.N );
            updateFlagsForSpot( nextSpot, true );

            nextSpot = getNextSpot( spot, Side.NW );
            updateFlagsForSpot( nextSpot, false );

            nextSpot = getNextSpot( spot, Side.NE );
            updateFlagsForSpot( nextSpot, false );
        }
    }

    private void updateFlagsForSpot( Spot spot, boolean validWhenFree )
    {
        if ( spot == null )
            return;

        if ( validWhenFree && spot.getPiece() == null )
            spot.setValidMoveFlg( true );
        else if ( !validWhenFree && spot.getPiece() != null && spot.getPiece().getColor() != sourcePiece.getColor() )
            spot.setValidMoveFlg( true );
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
