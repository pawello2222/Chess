package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;

// TODO: refactoring: class is too large

/**
 *  Move validator class implementation.
 *
 * @author Pawel Wiszenko
 */
class MoveValidatorImpl implements MoveValidator
{
    private Spot[][] spots;

    private Spot sourceSpot;
    private Piece sourcePiece;
    private PieceColor sourceColor;

    MoveValidatorImpl( Spot[][] spots )
    {
        this.spots = spots;
    }

    @Override
    public int getPossibleMovesCount()
    {
        int possibleMoves = 0;

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                possibleMoves += countValidMoveFlags( spots[ column ][ row ] );
                clearFlagsByType( FlagType.VALID_MOVE );
            }
        return possibleMoves;
    }

    private int countValidMoveFlags( Spot spot )
    {
        int count = 0;

        if ( spot == null || spot.isEmpty() || !spot.getPiece().isActive() )
            return 0;

        updateValidMoveFlags( spot );

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if ( spots[ column ][ row ].isValidMoveFlag() )
                    count++;

        return count;
    }

    @Override
    public void updateValidMoveFlags( Spot spot )
    {
        clearFlagsByType( FlagType.VALID_MOVE );

        if ( spot == null || spot.isEmpty() || !spot.getPiece().isActive() )
            return;

        sourceSpot = spot;
        sourcePiece = spot.getPiece();
        sourceColor = sourcePiece.getColor();

        switch ( sourcePiece.getType() )
        {
            case PAWN:
                updatePawnMoves( spot );
                break;

            case ROOK:
                updateLineMoves( spot, 0, 3 );
                break;

            case KNIGHT:
                updateKnightMoves( spot );
                break;

            case BISHOP:
                updateLineMoves( spot, 4, 7 );
                break;

            case QUEEN:
                updateLineMoves( spot, 0, 7 );
                break;

            case KING:
                updateKingMoves( spot );
                break;
        }
    }

    @Override
    public void updateFlagsAfterMove( Spot source, Spot target )
    {
        clearAllFlags();
        updateLastMoveFlags( source, target );
        updateCheckFlag();
        updatePawnSpecialMoveFlag( source, target );
    }

    private void updatePawnMoves( Spot spot )
    {
        Spot nextSpot;

        nextSpot = getNextSpot( spot, Direction.N, sourceColor );
        updateValidMoveFlag( nextSpot, true, false );

        if ( nextSpot != null && nextSpot.isEmpty() && sourcePiece.isUnmoved() )
        {
            nextSpot = getNextSpot( nextSpot, Direction.N, sourceColor );
            updateValidMoveFlag( nextSpot, true, false );
        }

        nextSpot = getNextSpot( spot, Direction.NW, sourceColor );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spot, Direction.NE, sourceColor );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spot, Direction.NW, sourceColor );
        updateEnPassantFlag( nextSpot, getNextSpot( spot, Direction.W, sourceColor ) );

        nextSpot = getNextSpot( spot, Direction.NE, sourceColor );
        updateEnPassantFlag( nextSpot, getNextSpot( spot, Direction.E, sourceColor ) );
    }

    private void updateLineMoves( Spot spot, int sideBegin, int sideEnd )
    {
        Spot nextSpot;

        for( int i = sideBegin; i <= sideEnd; i++ )
        {
            nextSpot = getNextSpot( spot, Direction.values()[ i ], sourceColor );

            while( nextSpot != null
                   && ( nextSpot.isEmpty() || nextSpot.getPiece().getColor() != sourcePiece.getColor() ) )
            {
                updateValidMoveFlag( nextSpot, true, true );

                if ( nextSpot.getPiece() != null && nextSpot.getPiece().getColor() != sourcePiece.getColor() )
                    break;

                nextSpot = getNextSpot( nextSpot, Direction.values()[ i ], sourceColor );
            }
        }
    }

    private void updateKnightMoves( Spot spot )
    {
        Spot nextSpot, oldSpot;

        for( int i = 0; i <= 3; i++ )
        {
            nextSpot = getNextSpot( spot, Direction.values()[ i ], sourceColor );
            oldSpot = getNextSpot( nextSpot, Direction.values()[ i ], sourceColor );

            nextSpot = getNextSpot( oldSpot, Direction.values()[ ( i + 1 ) % 4 ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );

            nextSpot = getNextSpot( oldSpot, Direction.values()[ ( i + 3 ) % 4 ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );
        }
    }

    private void updateKingMoves( Spot spot )
    {
        Piece kingPiece = spot.getPiece();

        for( int i = 0; i <= 7; i++ )
        {
            Spot nextSpot;

            nextSpot = getNextSpot( spot, Direction.values()[ i ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );
        }

        if ( kingPiece.isUnmoved() && !isSpotCapturable( spot, kingPiece.getColor() ) )
        {
            Spot[] nextSpots = new Spot[ 5 ];
            nextSpots[ 0 ] = spot;

            Direction directionE = sourcePiece.getColor() == PieceColor.WHITE ? Direction.E : Direction.W;
            Direction directionW = sourcePiece.getColor() == PieceColor.WHITE ? Direction.W : Direction.E;

            for ( int i = 1; i < 4; i ++ )
                nextSpots[ i ] = getNextSpot( nextSpots[ i - 1 ], directionE, sourceColor );

            if ( nextSpots[ 1 ].getPiece() == null && !isSpotCapturable( nextSpots[ 1 ], kingPiece.getColor() )
                 && nextSpots[ 2 ].getPiece() == null && !isSpotCapturable( nextSpots[ 2 ], kingPiece.getColor() )
                 && nextSpots[ 3 ].getPiece() != null && nextSpots[ 3 ].getPiece().isUnmoved() )
            {
                updateValidMoveFlag( nextSpots[ 2 ], true, false );
                nextSpots[ 2 ].setSpecialMoveFlag( true );
            }

            for ( int i = 1; i < 5; i ++ )
                nextSpots[ i ] = getNextSpot( nextSpots[ i - 1 ], directionW, sourceColor );

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

        if ( isSpotCapturable( getKingSpot( sourceColor ), sourceColor ) )
        {
            spot.setPiece( oldPiece );
            sourceSpot.setPiece( sourcePiece );
            return;
        }

        spot.setPiece( oldPiece );
        sourceSpot.setPiece( sourcePiece );

        boolean validFree = false;
        if ( spot.isEmpty() )
            validFree = true;

        boolean validOpponent = false;
        if ( spot.getPiece() != null && spot.getPiece().getColor() != sourceColor )
            validOpponent = true;

        if ( validWhenFree && validWhenOpponent )
        {
            if ( validFree || validOpponent )
                spot.setValidMoveFlag( true );
        }
        else if ( validWhenFree && validFree || validWhenOpponent && validOpponent )
            spot.setValidMoveFlag( true );
    }

    private void updateLastMoveFlags( Spot source, Spot target )
    {
        source.setLastMoveFlag( true );
        target.setLastMoveFlag( true );
    }

    private void updateCheckFlag()
    {
        PieceColor opponentColor = getOppositePieceColor( sourceColor );
        Spot kingSpot = getKingSpot( opponentColor );
        if ( kingSpot != null && isSpotCapturable( kingSpot, opponentColor ) )
            kingSpot.setCheckFlag( true );
    }

    private void updateEnPassantFlag( Spot target, Spot enPassant )
    {
        if ( target == null || enPassant == null )
            return;

        if ( enPassant.hasPieceType( PieceType.PAWN ) && enPassant.isSpecialMoveFlag() )
        {
            target.setValidMoveFlag( true );
            target.setEnPassantFlag( true );
        }
    }

    private void updatePawnSpecialMoveFlag( Spot source, Spot target )
    {
        if ( target.hasPieceType( PieceType.PAWN )
             && Math.abs( target.getRow() - source.getRow() ) == 2 )
            target.setSpecialMoveFlag( true );
    }

    private void clearAllFlags()
    {
        clearFlagsByType( FlagType.VALID_MOVE );
        clearFlagsByType( FlagType.LAST_MOVE );
        clearFlagsByType( FlagType.CHECK );
        clearFlagsByType( FlagType.EN_PASSANT );
        clearFlagsByType( FlagType.SPECIAL_MOVE );
    }

    private void clearFlagsByType( FlagType flagType )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                switch( flagType )
                {
                    case VALID_MOVE:
                        spots[ column ][ row ].setValidMoveFlag( false );
                        break;

                    case LAST_MOVE:
                        spots[ column ][ row ].setLastMoveFlag( false );
                        break;

                    case CHECK:
                        spots[ column ][ row ].setCheckFlag( false );
                        break;

                    case EN_PASSANT:
                        spots[ column ][ row ].setEnPassantFlag( false );
                        break;

                    case SPECIAL_MOVE:
                        spots[ column ][ row ].setSpecialMoveFlag( false );
                        break;
                }
    }

    private boolean isSpotCapturable( Spot spot, PieceColor pieceColor )
    {
        Spot nextSpot, tmpSpot, oldSpot;

        nextSpot = getNextSpot( spot, Direction.NW, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( pieceColor ) ) )
            return true;

        nextSpot = getNextSpot( spot, Direction.NE, pieceColor );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, getOppositePieceColor( pieceColor ) ) )
            return true;

        for( int i = 0; i <= 7; i++ )
        {
            nextSpot = getNextSpot( spot, Direction.values()[ i ], pieceColor );
            if ( isPieceAtSpot( nextSpot, PieceType.KING, getOppositePieceColor( pieceColor ) ) )
                return true;

            oldSpot = getNextSpot( nextSpot, Direction.values()[ i ], pieceColor );

            tmpSpot = getNextSpot( oldSpot, Direction.values()[ ( i + 1 ) % 4 ], pieceColor );
            if ( i <= 3 && isPieceAtSpot( tmpSpot, PieceType.KNIGHT, getOppositePieceColor( pieceColor ) ) )
                return true;

            tmpSpot = getNextSpot( oldSpot, Direction.values()[ ( i + 3 ) % 4 ], pieceColor );
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

                nextSpot = getNextSpot( nextSpot, Direction.values()[ i ], pieceColor );
            }
        }

        return false;
    }

    private Spot getNextSpot( Spot spot, Direction direction, PieceColor pieceColor )
    {
        if ( spot == null )
            return null;

        int diff = pieceColor == PieceColor.WHITE ? -1 : 1;

        switch( direction )
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
                return getNextSpot( getNextSpot( spot, Direction.N, pieceColor ), Direction.E, pieceColor );

            case SE:
                return getNextSpot( getNextSpot( spot, Direction.S, pieceColor ), Direction.E, pieceColor );

            case SW:
                return getNextSpot( getNextSpot( spot, Direction.S, pieceColor ), Direction.W, pieceColor );

            case NW:
                return getNextSpot( getNextSpot( spot, Direction.N, pieceColor ), Direction.W, pieceColor );
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
