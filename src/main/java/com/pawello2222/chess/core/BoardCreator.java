package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

import java.awt.*;
import java.util.List;

/**
 * Board creator.
 *
 * Helper class to initialize and populate board.
 *
 * @author Pawel Wiszenko
 */
public class BoardCreator
{
    private boolean boardReversed;

    public BoardCreator( boolean boardReversed )
    {
        this.boardReversed = boardReversed;
    }

    public void initializeSpots( Spot[][] spots )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                int x = boardReversed ? 7 - column : column;
                int y = boardReversed ? 7 - row : row;

                spots[ column ][ row ] = new Spot();
                spots[ column ][ row ].setX( Board.BOARD_OFFSET_X + Board.TILE_OFFSET_X * x );
                spots[ column ][ row ].setY( Board.BOARD_OFFSET_Y + Board.TILE_OFFSET_Y * y );
                spots[ column ][ row ].setRow( row );
                spots[ column ][ row ].setColumn( column );
            }
    }

    public void addPieces( Spot[][] spots, List< Piece > pieces )
    {
        for ( int j = 0; j < 2; j++ )
        {
            int row = j == 0 ? 6 : 1;
            PieceColor color = j == 0 ? PieceColor.WHITE : PieceColor.BLACK;

            for ( int i = 0; i < 8; i++ )
                createPiece( spots, pieces, row, i, color, PieceType.PAWN );

            row = j == 0 ? 7 : 0;
            createPiece( spots, pieces, row, 0, color, PieceType.ROOK );
            createPiece( spots, pieces, row, 1, color, PieceType.KNIGHT );
            createPiece( spots, pieces, row, 2, color, PieceType.BISHOP );
            createPiece( spots, pieces, row, 3, color, PieceType.QUEEN );
            createPiece( spots, pieces, row, 4, color, PieceType.KING );
            createPiece( spots, pieces, row, 5, color, PieceType.BISHOP );
            createPiece( spots, pieces, row, 6, color, PieceType.KNIGHT );
            createPiece( spots, pieces, row, 7, color, PieceType.ROOK );
        }
    }

    private void createPiece( Spot[][] spots, List< Piece > pieces, int row, int column,
                              PieceColor color, PieceType type )
            throws InvalidResourceException
    {
        Image pieceImage = Board.loadResource( color + "_" + type + ".png" );

        boolean active = false;

        Piece piece = new Piece( spots[ column ][ row ], pieceImage, color, type,
                                 color == PieceColor.WHITE, boardReversed );
        pieces.add( piece );
        spots[ column ][ row ].setPiece( piece );
    }
}
