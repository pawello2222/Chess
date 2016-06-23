package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;

import static com.pawello2222.chess.utils.SpotUtils.*;

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

        nextSpot = getNextSpot( spots, spot, Direction.N, sourceColor );
        updateValidMoveFlag( nextSpot, true, false );

        if ( nextSpot != null && nextSpot.isEmpty() && sourcePiece.isUnmoved() )
        {
            nextSpot = getNextSpot( spots, nextSpot, Direction.N, sourceColor );
            updateValidMoveFlag( nextSpot, true, false );
        }

        nextSpot = getNextSpot( spots, spot, Direction.NW, sourceColor );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spots, spot, Direction.NE, sourceColor );
        updateValidMoveFlag( nextSpot, false, true );

        nextSpot = getNextSpot( spots, spot, Direction.NW, sourceColor );
        updateEnPassantFlag( nextSpot, getNextSpot( spots, spot, Direction.W, sourceColor ) );

        nextSpot = getNextSpot( spots, spot, Direction.NE, sourceColor );
        updateEnPassantFlag( nextSpot, getNextSpot( spots, spot, Direction.E, sourceColor ) );
    }

    private void updateLineMoves( Spot spot, int sideBegin, int sideEnd )
    {
        Spot nextSpot;

        for( int i = sideBegin; i <= sideEnd; i++ )
        {
            nextSpot = getNextSpot( spots, spot, Direction.values()[ i ], sourceColor );

            PieceColor opponentColor = getOppositePieceColor( sourceColor );
            while( nextSpot != null && ( nextSpot.isEmpty() || nextSpot.hasPieceColor( opponentColor ) ) )
            {
                updateValidMoveFlag( nextSpot, true, true );

                if ( !nextSpot.isEmpty() && nextSpot.hasPieceColor( opponentColor ) )
                    break;

                nextSpot = getNextSpot( spots, nextSpot, Direction.values()[ i ], sourceColor );
            }
        }
    }

    private void updateKnightMoves( Spot spot )
    {
        Spot nextSpot, oldSpot;

        for( int i = 0; i <= 3; i++ )
        {
            nextSpot = getNextSpot( spots, spot, Direction.values()[ i ], sourceColor );
            oldSpot = getNextSpot( spots, nextSpot, Direction.values()[ i ], sourceColor );

            nextSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 1 ) % 4 ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );

            nextSpot = getNextSpot( spots, oldSpot, Direction.values()[ ( i + 3 ) % 4 ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );
        }
    }

    private void updateKingMoves( Spot spot )
    {
        Piece kingPiece = spot.getPiece();

        for( int i = 0; i <= 7; i++ )
        {
            Spot nextSpot;

            nextSpot = getNextSpot( spots, spot, Direction.values()[ i ], sourceColor );
            updateValidMoveFlag( nextSpot, true, true );
        }

        if ( kingPiece.isUnmoved() && !isSpotCapturable( spots, spot, kingPiece.getColor() ) )
        {
            Spot[] nextSpots = new Spot[ 5 ];
            nextSpots[ 0 ] = spot;

            Direction directionE = sourceColor == PieceColor.WHITE ? Direction.E : Direction.W;
            Direction directionW = sourceColor == PieceColor.WHITE ? Direction.W : Direction.E;

            for ( int i = 1; i < 4; i ++ )
                nextSpots[ i ] = getNextSpot( spots, nextSpots[ i - 1 ], directionE, sourceColor );

            if ( nextSpots[ 1 ].isEmpty() && !isSpotCapturable( spots, nextSpots[ 1 ], kingPiece.getColor() )
                 && nextSpots[ 2 ].isEmpty() && !isSpotCapturable( spots, nextSpots[ 2 ], kingPiece.getColor() )
                 && !nextSpots[ 3 ].isEmpty() && nextSpots[ 3 ].getPiece().isUnmoved() )
            {
                updateValidMoveFlag( nextSpots[ 2 ], true, false );
                nextSpots[ 2 ].setSpecialMoveFlag( true );
            }

            for ( int i = 1; i < 5; i ++ )
                nextSpots[ i ] = getNextSpot( spots, nextSpots[ i - 1 ], directionW, sourceColor );

            if ( nextSpots[ 1 ].isEmpty() && !isSpotCapturable( spots, nextSpots[ 1 ], kingPiece.getColor() )
                 && nextSpots[ 2 ].isEmpty() && !isSpotCapturable( spots, nextSpots[ 2 ], kingPiece.getColor() )
                 && nextSpots[ 3 ].isEmpty()
                 && !nextSpots[ 4 ].isEmpty() && nextSpots[ 4 ].getPiece().isUnmoved() )
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

        if ( isSpotCapturable( spots, getKingSpot( spots, sourceColor ), sourceColor ) )
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
        PieceColor opponentColor = getOppositePieceColor( sourceColor );
        if ( !spot.isEmpty() && spot.hasPieceColor( opponentColor ) )
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
        Spot kingSpot = getKingSpot( spots, opponentColor );
        if ( kingSpot != null && isSpotCapturable( spots, kingSpot, opponentColor ) )
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
}
