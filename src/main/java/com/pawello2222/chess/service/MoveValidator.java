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
    public void validateMovesForSpot( Spot spot )
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
                updateRookMoves( spot, isColorWhite );
                break;

            case KNIGHT:
                updateKnightMoves( spot, isColorWhite );
                break;

            case BISHOP:
                updateBishopMoves( spot, isColorWhite );
                break;

            case QUEEN:
                updateQueenMoves( spot, isColorWhite );
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

        nextSpot = getNextSpot( spot, Side.W, isColorWhite );
        updateEnPassantFlag( nextSpot );

        nextSpot = getNextSpot( spot, Side.E, isColorWhite );
        updateEnPassantFlag( nextSpot );
    }

    private void updateRookMoves( Spot spot, boolean isColorWhite )
    {
        Spot nextSpot;

        for( int i = 0; i < 4; i++ )
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

    }

    private void updateBishopMoves( Spot spot, boolean isColorWhite )
    {

    }

    private void updateQueenMoves( Spot spot, boolean isColorWhite )
    {

    }

    private void updateKingMoves( Spot spot, boolean isColorWhite )
    {

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
    public void updateCheckFlag()
    {
        PieceColor opponentColor = PieceLogic.getOppositePieceColor( sourcePiece.getColor() );
        Spot kingSpot = getKingSpot( opponentColor );
        if ( kingSpot != null && isSpotCapturable( kingSpot, opponentColor ) )
            kingSpot.setCheckFlag( true );
    }

    private void updateEnPassantFlag( Spot spot )
    {
        if ( spot == null )
            return;

        Piece targetPiece = spot.getPiece();
        if ( targetPiece != null && targetPiece.getColor() != sourcePiece.getColor() && spot.isEnPassantFlag() )
            getNextSpot( spot, Side.N, sourcePiece.getColor() == PieceColor.WHITE ).setValidMoveFlag( true );
    }

    @Override
    public int countMovesForSpot( Spot spot )
    {
        int count = 0;

        if ( spot == null || spot.getPiece() == null || !spot.getPiece().isActive() )
            return 0;

        validateMovesForSpot( spot );

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if ( spots[ column ][ row ].isValidMoveFlag() )
                    count++;

        return count;
    }

    private boolean isSpotCapturable( Spot spot, PieceColor color )
    {
        Spot nextSpot;

        nextSpot = getNextSpot( spot, Side.NW, color == PieceColor.WHITE );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, PieceLogic.getOppositePieceColor( color ) ) )
            return true;

        nextSpot = getNextSpot( spot, Side.NE, color == PieceColor.WHITE );
        if ( isPieceAtSpot( nextSpot, PieceType.PAWN, PieceLogic.getOppositePieceColor( color ) ) )
            return true;

        for( int i = 0; i < 4; i++ )
        {
            nextSpot = getNextSpot( spot, Side.values()[ i ], color == PieceColor.WHITE );

            while( nextSpot != null && ( nextSpot.getPiece() == null || nextSpot.getPiece().getColor() != color ) )
            {
                if ( nextSpot.getPiece() != null && nextSpot.getPiece().getColor() != color )
                {
                    if( nextSpot.getPiece().getType() == PieceType.ROOK
                        || nextSpot.getPiece().getType() == PieceType.QUEEN )
                        return true;
                    else
                        break;
                }

                nextSpot = getNextSpot( nextSpot, Side.values()[ i ], color == PieceColor.WHITE );
            }
        }

        return false;
    }

    private boolean isPieceAtSpot( Spot spot, PieceType type, PieceColor color )
    {
        return spot != null
               && spot.getPiece() != null
               && spot.getPiece().getType() == type
               && spot.getPiece().getColor() == color;
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
}
