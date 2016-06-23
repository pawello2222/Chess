package com.pawello2222.chess.utils;

import com.pawello2222.chess.model.Direction;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

/**
 * Spot and piece utils.
 *
 * @author Pawel Wiszenko
 */
public abstract class SpotUtils
{
    public static boolean isSpotCapturable( Spot[][] spots, Spot spot, PieceColor pieceColor )
    {
        Spot nextSpot, tmpSpot, oldSpot;

        nextSpot = getNextSpot( spots, spot, Direction.NW, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( pieceColor ) ) )
            return true;

        nextSpot = getNextSpot( spots, spot, Direction.NE, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( pieceColor ) ) )
            return true;

        for( int i = 0; i <= 7; i++ )
        {
            nextSpot = getNextSpot( spots, spot, Direction.values()[ i ], pieceColor );
            if ( isPieceAtSpot( nextSpot, PieceType.KING, getOppositePieceColor( pieceColor ) ) )
                return true;

            oldSpot = getNextSpot( spots, nextSpot, Direction.values()[ i ], pieceColor );

            tmpSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 1 ) % 4 ], pieceColor );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, getOppositePieceColor( pieceColor ) ) )
                return true;

            tmpSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 3 ) % 4 ], pieceColor );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, getOppositePieceColor( pieceColor ) ) )
                return true;

            while( nextSpot != null && ( nextSpot.isEmpty() || nextSpot.getPiece().getColor() != pieceColor ) )
            {
                if ( nextSpot.getPiece() != null && nextSpot.getPiece().getColor() != pieceColor )
                {
                    if( nextSpot.getPiece().getType() == PieceType.QUEEN
                        || nextSpot.getPiece().getType() == PieceType.ROOK && i <= 3
                        || nextSpot.getPiece().getType() == PieceType.BISHOP && i >= 4 )
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

    public static Spot getKingSpot( Spot[][] spots, PieceColor pieceColor )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if( spots[ column ][ row ].getPiece() != null
                    && spots[ column ][ row ].getPiece().getType() == PieceType.KING
                    && spots[ column ][ row ].getPiece().getColor() == pieceColor )

                    return spots[ column ][ row ];

        return null;
    }

    public static PieceColor getOppositePieceColor( PieceColor pieceColor )
    {
        return pieceColor == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static boolean isPieceAtSpot( Spot spot, PieceType type, PieceColor color )
    {
        return spot != null
               && spot.getPiece() != null
               && spot.getPiece().getType() == type
               && spot.getPiece().getColor() == color;
    }
}
