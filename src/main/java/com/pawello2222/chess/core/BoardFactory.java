package com.pawello2222.chess.core;

import com.pawello2222.chess.core.gui.BoardPanel;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Board factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class BoardFactory
{
    public static JPanel getBoardPanel( Image image, Spot[][] spots, List< Piece > pieces )
    {
        return new BoardPanel( image, spots, pieces );
    }

    public static Spot[][] getSpots( boolean reversed )
    {
        Spot[][] spots = new Spot[ 8 ][ 8 ];

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                int x = reversed ? 7 - column : column;
                int y = reversed ? 7 - row : row;

                spots[ column ][ row ] = new Spot();
                spots[ column ][ row ].setX( BoardPanel.BOARD_OFFSET_X + Spot.SPOT_WIDTH * x );
                spots[ column ][ row ].setY( BoardPanel.BOARD_OFFSET_Y + Spot.SPOT_HEIGHT * y );
                spots[ column ][ row ].setRow( row );
                spots[ column ][ row ].setColumn( column );
            }

        return spots;
    }

    public static List< Piece > getPieces( Spot[][] spots )
    {
        List< Piece > pieces = new ArrayList<>();

        for ( int j = 0; j < 2; j++ )
        {
            int row = j == 0 ? 6 : 1;
            PieceColor color = j == 0 ? PieceColor.WHITE : PieceColor.BLACK;

            for ( int i = 0; i < 8; i++ )
                pieces.add( getPiece( spots, row, i, color, PieceType.PAWN ) );

            row = j == 0 ? 7 : 0;
            pieces.add( getPiece( spots, row, 0, color, PieceType.ROOK ) );
            pieces.add( getPiece( spots, row, 1, color, PieceType.KNIGHT ) );
            pieces.add( getPiece( spots, row, 2, color, PieceType.BISHOP ) );
            pieces.add( getPiece( spots, row, 3, color, PieceType.QUEEN ) );
            pieces.add( getPiece( spots, row, 4, color, PieceType.KING ) );
            pieces.add( getPiece( spots, row, 5, color, PieceType.BISHOP ) );
            pieces.add( getPiece( spots, row, 6, color, PieceType.KNIGHT ) );
            pieces.add( getPiece( spots, row, 7, color, PieceType.ROOK ) );
        }

        return pieces;
    }

    private static Piece getPiece( Spot[][] spots, int row, int column, PieceColor color, PieceType type )
    {
        Image pieceImage = ResourceLoader.loadImageExitOnEx( color + "_" + type + ".png" );
        Piece piece = new Piece( pieceImage, color, type, false );
        spots[ column ][ row ].setPiece( piece );
        piece.setCoordinatesToSpot( spots[ column ][ row ] );

        return piece;
    }
}
