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
        if ( spot == null || spot.getPiece() == null || !spot.getPiece().isActive() )
            return;

        sourcePiece = spot.getPiece();

        boolean isColorWhite = sourcePiece.getColor() == PieceColor.WHITE;

        switch ( sourcePiece.getType() )
        {
            case PAWN:
                updatePawnMoves( spot, isColorWhite );
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

    @Override
    public boolean updateCheckFlag()
    {
        PieceColor opponentColor = PieceLogic.getOppositePieceColor( sourcePiece.getColor() );
        Spot kingSpot = getKingSpot( opponentColor );
        if ( kingSpot != null && isSpotCapturable( kingSpot, opponentColor ) )
            kingSpot.setCheckFlag( true );

        return false;
    }

    private void updatePawnMoves( Spot spot, boolean isColorWhite )
    {
        Spot nextSpot;

        nextSpot = getNextSpot( spot, Side.N, isColorWhite );
        updateValidFlag( nextSpot, true );

        if ( nextSpot != null && nextSpot.getPiece() == null && sourcePiece.isUnmoved() )
        {
            nextSpot = getNextSpot( nextSpot, Side.N, isColorWhite );
            updateValidFlag( nextSpot, true );
        }

        nextSpot = getNextSpot( spot, Side.NW, isColorWhite );
        updateValidFlag( nextSpot, false );

        nextSpot = getNextSpot( spot, Side.NE, isColorWhite );
        updateValidFlag( nextSpot, false );

        nextSpot = getNextSpot( spot, Side.NE, isColorWhite );
        updateValidFlag( nextSpot, false );

        nextSpot = getNextSpot( spot, Side.W, isColorWhite );
        updateEnPassantFlag( nextSpot );

        nextSpot = getNextSpot( spot, Side.E, isColorWhite );
        updateEnPassantFlag( nextSpot );
    }

    private void updateValidFlag( Spot spot, boolean validWhenFree )
    {
        if ( spot == null )
            return;

        PieceColor activeColor = sourcePiece.getColor();
        if ( isSpotCapturable( getKingSpot( activeColor ), activeColor ) )
            return;

        if ( validWhenFree && spot.getPiece() == null )
            spot.setValidMoveFlag( true );
        else if ( !validWhenFree && spot.getPiece() != null && spot.getPiece().getColor() != activeColor )
            spot.setValidMoveFlag( true );
    }

    private void updateEnPassantFlag( Spot spot )
    {
        if ( spot == null )
            return;

        Piece targetPiece = spot.getPiece();
        if ( targetPiece != null && targetPiece.getColor() != sourcePiece.getColor() && spot.isEnPassantFlag() )
            getNextSpot( spot, Side.N, sourcePiece.getColor() == PieceColor.WHITE ).setValidMoveFlag( true );
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
