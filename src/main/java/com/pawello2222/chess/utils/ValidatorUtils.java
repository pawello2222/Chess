package com.pawello2222.chess.utils;

import com.pawello2222.chess.model.Direction;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

/**
 * Validator utils.
 *
 * @author Pawel Wiszenko
 */
public abstract class ValidatorUtils
{
    public static boolean isSpotCapturable( Spot[][] spots, Spot spot, PieceColor pieceColor )
    {
        Spot nextSpot, tmpSpot, oldSpot;
        PieceColor opponentColor = getOppositePieceColor( pieceColor );

        nextSpot = getNextSpot( spots, spot, Direction.NW, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, opponentColor ) )
            return true;

        nextSpot = getNextSpot( spots, spot, Direction.NE, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, opponentColor ) )
            return true;

        for( int i = 0; i <= 7; i++ )
        {
            nextSpot = getNextSpot( spots, spot, Direction.values()[ i ], pieceColor );
            if ( isPieceAtSpot( nextSpot, PieceType.KING, opponentColor ) )
                return true;

            oldSpot = getNextSpot( spots, nextSpot, Direction.values()[ i ], pieceColor );

            tmpSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 1 ) % 4 ], pieceColor );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, opponentColor ) )
                return true;

            tmpSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 3 ) % 4 ], pieceColor );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, opponentColor ) )
                return true;

            while( nextSpot != null && ( nextSpot.isEmpty() || nextSpot.hasPieceColor( opponentColor ) ) )
            {
                if ( nextSpot.hasPieceColor( opponentColor ) )
                {
                    if( nextSpot.hasPieceType( PieceType.QUEEN )
                        || nextSpot.hasPieceType( PieceType.ROOK ) && i <= 3
                        || nextSpot.hasPieceType( PieceType.BISHOP ) && i >= 4 )
                        return true;
                    else
                        break;
                }

                nextSpot = getNextSpot( spots, nextSpot, Direction.values()[ i ], pieceColor );
            }
        }

        return false;
    }

    public static Spot getNextSpot( Spot[][] spots, Spot startSpot, Direction direction, PieceColor pieceColor )
    {
        if ( startSpot == null )
            return null;

        int diff = pieceColor == PieceColor.WHITE ? -1 : 1;

        switch( direction )
        {
            case N:
                if ( startSpot.getRow() + diff < 0 || startSpot.getRow() + diff > 7 )
                    break;
                return spots[ startSpot.getColumn() ][ startSpot.getRow() + diff ];

            case E:
                if ( startSpot.getColumn() - diff < 0 || startSpot.getColumn() - diff > 7 )
                    break;
                return spots[ startSpot.getColumn() - diff ][ startSpot.getRow() ];

            case S:
                if ( startSpot.getRow() - diff < 0 || startSpot.getRow() - diff > 7 )
                    break;
                return spots[ startSpot.getColumn() ][ startSpot.getRow() - diff ];

            case W:
                if ( startSpot.getColumn() + diff < 0 || startSpot.getColumn() + diff > 7 )
                    break;
                return spots[ startSpot.getColumn() + diff ][ startSpot.getRow() ];

            case NE:
                return getNextSpot( spots,
                                    getNextSpot( spots, startSpot, Direction.N, pieceColor ),
                                    Direction.E,
                                    pieceColor );

            case SE:
                return getNextSpot( spots,
                                    getNextSpot( spots, startSpot, Direction.S, pieceColor ),
                                    Direction.E,
                                    pieceColor );

            case SW:
                return getNextSpot( spots,
                                    getNextSpot( spots, startSpot, Direction.S, pieceColor ),
                                    Direction.W,
                                    pieceColor );

            case NW:
                return getNextSpot( spots,
                                    getNextSpot( spots, startSpot, Direction.N, pieceColor ),
                                    Direction.W,
                                    pieceColor );
        }

        return null;
    }

    public static Spot getKingSpot( Spot[][] spots, PieceColor kingColor )
    {
        for ( Spot[] row: spots )
            for ( Spot spot : row )
                if( spot.hasPieceType( PieceType.KING ) && spot.hasPieceColor( kingColor ) )
                    return spot;

        return null;
    }

    public static PieceColor getOppositePieceColor( PieceColor pieceColor )
    {
        return pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static boolean isPieceAtSpot( Spot spot, PieceType type, PieceColor color )
    {
        return spot != null && spot.hasPieceType( type ) && spot.hasPieceColor( color );
    }
}
