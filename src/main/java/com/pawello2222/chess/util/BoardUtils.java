package com.pawello2222.chess.util;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * BoardPanel utilities.
 *
 * @author Pawel Wiszenko
 */
public abstract class BoardUtils
{
    public static void executeMove( Spot[][] spots, List< Piece > pieces, Spot sourceSpot, Spot targetSpot )
    {
        Piece sourcePiece = sourceSpot.getPiece();

        if ( sourcePiece.isUnmoved() )
            sourcePiece.setUnmoved( false );

        if ( targetSpot.isEnPassantFlag() )
        {
            pieces.remove( spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].getPiece() );
            spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].setPiece( null );
        }
        else if ( targetSpot.isSpecialMoveFlag() && targetSpot.isEmpty() )
        {
            Spot source = spots[ targetSpot.getColumn() == 2 ? 0 : 7 ][ targetSpot.getRow() ];
            Spot target = spots[ targetSpot.getColumn() == 2 ? 3 : 5 ][ targetSpot.getRow() ];

            target.setPiece( source.getPiece() );
            target.getPiece().setCoordinatesToSpot( target );
            source.setPiece( null );
        }
        else if ( targetSpot.getPiece() != null )
            pieces.remove( targetSpot.getPiece() );

        targetSpot.setPiece( sourcePiece );
        targetSpot.getPiece().setCoordinatesToSpot( targetSpot );
        sourceSpot.setPiece( null );
    }

    public static char checkPawnPromotion( Container container, Spot spot, char promotion )
    {
        int promotionRow = spot.hasPieceColor( PieceColor.WHITE ) ? 0 : 7;
        if ( spot.hasPieceType( PieceType.PAWN ) && spot.getRow() == promotionRow )
            return promotePawn( container, spot.getPiece(), promotion );

        return promotion;
    }

    private static char promotePawn( Container container, Piece piece, char promotion )
    {
        PieceType promotionType;

        if ( promotion == 'X' )
        {
            String chosenType;
            do
                chosenType = getPromotionDialogResult( container );
            while ( chosenType == null );
            promotionType = PieceType.valueOf( chosenType.toUpperCase() );
        }
        else
        {
            int index = Character.getNumericValue( promotion );
            promotionType = PieceType.values()[ index ];
        }

        String path = piece.getColor() + "_" + promotionType + ".png";
        piece.setImage( ResourceLoader.loadImageExitOnEx( path ) );
        piece.setType( promotionType );

        return Character.forDigit( promotionType.ordinal(), 10 );
    }

    private static String getPromotionDialogResult( Container container )
    {
        Object[] possibilities = { "Knight", "Bishop", "Rook", "Queen" };

        return ( String ) JOptionPane.showInputDialog(
                container, "Choose promotion", "Promotion",
                JOptionPane.PLAIN_MESSAGE, null,
                possibilities, possibilities[ 3 ] );
    }

    public static boolean isCheckFlagSet( Spot[][] spots )
    {
        for ( Spot[] row: spots )
            for ( Spot spot : row )
                if ( spot.isCheckFlag() )
                    return true;

        return false;
    }
}
