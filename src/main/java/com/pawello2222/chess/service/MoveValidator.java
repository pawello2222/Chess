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

    private Spot sourceSpot;
    private Piece sourcePiece;

    public MoveValidator( Spot[][] spots )
    {
        this.spots = spots;
    }

    @Override
    public void updateValidMoveFlags( Spot spot )
    {
        if ( spot == null || spot.getPiece() == null || !spot.getPiece().isActive() )
            return;

        sourceSpot = spot;
        sourcePiece = spot.getPiece();

        boolean isColorWhite = sourcePiece.getColor() == PieceColor.WHITE;

        switch ( sourcePiece.getType() )
        {
            case PAWN:
                updatePawnMoves( spot, isColorWhite );
                break;

            case ROOK:
                updateLineMoves( spot, isColorWhite, 0, 3 );
                break;

            case KNIGHT:
                updateKnightMoves( spot, isColorWhite );
                break;

            case BISHOP:
                updateLineMoves( spot, isColorWhite, 4, 7 );
                break;

            case QUEEN:
                updateLineMoves( spot, isColorWhite, 0, 7 );
                break;

            case KING:
                updateKingMoves( spot, isColorWhite );
                break;
        }
    }

    private void updatePawnMoves( Spot spot, boolean isColorWhite )
    {
        Spot nextSpot;

        nextSpot = getNextSpot( spot, Side.N, isColorWhite );
        updateValidMoveFlag( nextSpot, true, false );

        if ( nextSpot != null && nextSpot.getPiece() == null && sourcePiece.isUnmoved() )
        {
            nextSpot = getNextSpot( nextSpot, Side.N, isColorWhite );
            updateValidMoveFlag( nextSpot, true, false );
        }

        nextSpot = getNextSpot( spot, Side.NW, isColorWhite );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spot, Side.NE, isColorWhite );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spot, Side.NE, isColorWhite );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spot, Side.NW, isColorWhite );
        updateEnPassantFlag( nextSpot, getNextSpot( spot, Side.W, isColorWhite ) );

        nextSpot = getNextSpot( spot, Side.NE, isColorWhite );
        updateEnPassantFlag( nextSpot, getNextSpot( spot, Side.E, isColorWhite ) );
    }

    private void updateLineMoves( Spot spot, boolean isColorWhite, int sideBegin, int sideEnd )
    {
        Spot nextSpot;

        for( int i = sideBegin; i <= sideEnd; i++ )
        {
            nextSpot = getNextSpot( spot, Side.values()[ i ], isColorWhite );

            while( nextSpot != null
                   && ( nextSpot.getPiece() == null || nextSpot.getPiece().getColor() != sourcePiece.getColor() ) )
            {
                updateValidMoveFlag( nextSpot, true, true );

                if ( nextSpot.getPiece() != null && nextSpot.getPiece().getColor() != sourcePiece.getColor() )
                    break;

                nextSpot = getNextSpot( nextSpot, Side.values()[ i ], isColorWhite );
            }
        }
    }

    private void updateKnightMoves( Spot spot, boolean isColorWhite )
    {
        Spot nextSpot, oldSpot;

        for( int i = 0; i <= 3; i++ )
        {
            nextSpot = getNextSpot( spot, Side.values()[ i ], isColorWhite );
            oldSpot = getNextSpot( nextSpot, Side.values()[ i ], isColorWhite );

            nextSpot = getNextSpot( oldSpot, Side.values()[ ( i + 1 ) % 4 ], isColorWhite );
            updateValidMoveFlag( nextSpot, true, true );

            nextSpot = getNextSpot( oldSpot, Side.values()[ ( i + 3 ) % 4 ], isColorWhite );
            updateValidMoveFlag( nextSpot, true, true );
        }
    }

    private void updateKingMoves( Spot spot, boolean isColorWhite )
    {
        Piece kingPiece = spot.getPiece();

        for( int i = 0; i <= 7; i++ )
        {
            Spot nextSpot;

            nextSpot = getNextSpot( spot, Side.values()[ i ], isColorWhite );
            updateValidMoveFlag( nextSpot, true, true );
        }

        if ( kingPiece.isUnmoved() && !isSpotCapturable( spot, kingPiece.getColor() ) )
        {
            Spot[] nextSpots = new Spot[ 5 ];
            nextSpots[ 0 ] = spot;

            Side sideE = isColorWhite ? Side.E : Side.W;
            Side sideW = isColorWhite ? Side.W : Side.E;

            for ( int i = 1; i < 4; i ++ )
                nextSpots[ i ] = getNextSpot( nextSpots[ i - 1 ], sideE, isColorWhite );

            if ( nextSpots[ 1 ].getPiece() == null && !isSpotCapturable( nextSpots[ 1 ], kingPiece.getColor() )
                 && nextSpots[ 2 ].getPiece() == null && !isSpotCapturable( nextSpots[ 2 ], kingPiece.getColor() )
                 && nextSpots[ 3 ].getPiece() != null && nextSpots[ 3 ].getPiece().isUnmoved() )
            {
                updateValidMoveFlag( nextSpots[ 2 ], true, false );
                nextSpots[ 2 ].setSpecialMoveFlag( true );
            }

            for ( int i = 1; i < 5; i ++ )
                nextSpots[ i ] = getNextSpot( nextSpots[ i - 1 ], sideW, isColorWhite );

            if ( nextSpots[ 1 ].getPiece() == null && !isSpotCapturable( nextSpots[ 1 ], kingPiece.getColor() )
                 && nextSpots[ 2 ].getPiece() == null && !isSpotCapturable( nextSpots[ 2 ], kingPiece.getColor() )
                 && nextSpots[ 3 ].getPiece() == null
                 && nextSpots[ 4 ].getPiece() != null && nextSpots[ 4 ].getPiece().isUnmoved() )
            {
                updateValidMoveFlag( nextSpots[ 2 ], true, false );
                nextSpots[ 2 ].setSpecialMoveFlag( true );
            }
        }
    }

    private void updateValidMoveFlag( Spot spot, boolean validWhenFree, boolean validWhenOpponent )
    {
        if ( spot == null )
            return;

        Piece oldPiece = spot.getPiece();
        spot.setPiece( sourcePiece );
        sourceSpot.setPiece( null );

        PieceColor activeColor = sourcePiece.getColor();
        if ( isSpotCapturable( getKingSpot( activeColor ), activeColor ) )
        {
            spot.setPiece( oldPiece );
            sourceSpot.setPiece( sourcePiece );
            return;
        }

        spot.setPiece( oldPiece );
        sourceSpot.setPiece( sourcePiece );

        boolean validFree = false;
        if ( spot.getPiece() == null )
            validFree = true;

        boolean validOpponent = false;
        if ( spot.getPiece() != null && spot.getPiece().getColor() != activeColor )
            validOpponent = true;

        if ( validWhenFree && validWhenOpponent )
        {
            if ( validFree || validOpponent )
                spot.setValidMoveFlag( true );
        }
        else if ( validWhenFree && validFree || validWhenOpponent && validOpponent )
            spot.setValidMoveFlag( true );
    }

    @Override
    public void updateLastMoveFlags( Spot source, Spot target )
    {
        source.setLastMoveFlag( true );
        target.setLastMoveFlag( true );
    }

    @Override
    public void updateCheckFlag()
    {
        PieceColor opponentColor = getOppositePieceColor( sourcePiece.getColor() );
        Spot kingSpot = getKingSpot( opponentColor );
        if ( kingSpot != null && isSpotCapturable( kingSpot, opponentColor ) )
            kingSpot.setCheckFlag( true );
    }

    @Override
    public void updateSpecialMoveFlag( Spot source, Spot target )
    {
        if ( target.getPiece().getType() == PieceType.PAWN
             && Math.abs( target.getRow() - source.getRow() ) == 2 )
            target.setSpecialMoveFlag( true );
    }

    private void updateEnPassantFlag( Spot target, Spot enPassant )
    {
        if ( target == null || enPassant == null )
            return;

        if ( target.getPiece() == null
             && enPassant.getPiece() != null
             && enPassant.getPiece().getType() == PieceType.PAWN
             && enPassant.isSpecialMoveFlag() )
        {
            target.setValidMoveFlag( true );
            target.setEnPassantFlag( true );
        }
    }

    private boolean isSpotCapturable( Spot spot, PieceColor color )
    {
        Spot nextSpot, tmpSpot, oldSpot;

        nextSpot = getNextSpot( spot, Side.NW, color == PieceColor.WHITE );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( color ) ) )
            return true;

        nextSpot = getNextSpot( spot, Side.NE, color == PieceColor.WHITE );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( color ) ) )
            return true;

        for( int i = 0; i <= 7; i++ )
        {
            nextSpot = getNextSpot( spot, Side.values()[ i ], color == PieceColor.WHITE );
            if ( isPieceAtSpot( nextSpot, PieceType.KING, getOppositePieceColor( color ) ) )
                return true;

            oldSpot = getNextSpot( nextSpot, Side.values()[ i ], color == PieceColor.WHITE );

            tmpSpot = getNextSpot( oldSpot, Side.values()[ ( i + 1 ) % 4 ], color == PieceColor.WHITE );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, getOppositePieceColor( color ) ) )
                return true;

            tmpSpot = getNextSpot( oldSpot, Side.values()[ ( i + 3 ) % 4 ], color == PieceColor.WHITE );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, getOppositePieceColor( color ) ) )
                return true;

            while( nextSpot != null && ( nextSpot.getPiece() == null || nextSpot.getPiece().getColor() != color ) )
            {
                if ( nextSpot.getPiece() != null && nextSpot.getPiece().getColor() != color )
                {
                    if( nextSpot.getPiece().getType() == PieceType.QUEEN
                        || nextSpot.getPiece().getType() == PieceType.ROOK && i <= 3
                        || nextSpot.getPiece().getType() == PieceType.BISHOP && i >= 4 )
                        return true;
                    else
                        break;
                }

                nextSpot = getNextSpot( nextSpot, Side.values()[ i ], color == PieceColor.WHITE );
            }
        }

        return false;
    }

    private Spot getNextSpot( Spot spot, Side side, boolean isColorWhite )
    {
        if ( spot == null )
            return null;

        int diff = isColorWhite ? -1 : 1;

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
                return getNextSpot( getNextSpot( spot, Side.N, isColorWhite ), Side.E, isColorWhite );

            case SE:
                return getNextSpot( getNextSpot( spot, Side.S, isColorWhite ), Side.E, isColorWhite );

            case SW:
                return getNextSpot( getNextSpot( spot, Side.S, isColorWhite ), Side.W, isColorWhite );

            case NW:
                return getNextSpot( getNextSpot( spot, Side.N, isColorWhite ), Side.W, isColorWhite );
        }

        return null;
    }

    private Spot getKingSpot( PieceColor pieceColor )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if( spots[ column ][ row ].getPiece() != null
                    && spots[ column ][ row ].getPiece().getType() == PieceType.KING
                    && spots[ column ][ row ].getPiece().getColor() == pieceColor )

                    return spots[ column ][ row ];

        return null;
    }

    private static PieceColor getOppositePieceColor( PieceColor pieceColor )
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
